/*
 * Copyright (C) 1993-1996 Id Software, Inc.
 * Copyright (C) 2017 Good Sign
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package p;

import doom.SourceCode.D_Think;
import doom.SourceCode.D_Think.actionf_t;
import doom.SourceCode.actionf_p1;
import doom.SourceCode.actionf_p2;
import doom.SourceCode.actionf_v;
import doom.player_t;
import doom.thinker_t;
import ktdoom.LoggersKt;
import p.Actions.ActiveStates.Thinkers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In vanilla doom there is union called actionf_t that can hold
 * one of the three types: actionf_p1, actionf_v and actionf_p2
 * 
 * typedef union
 * {
 *   actionf_p1	acp1;
 *   actionf_v	acv;
 *   actionf_p2	acp2;
 *
 * } actionf_t;
 * 
 * For those unfamiliar with C, the union can have only one value
 * assigned with all the values combined solving the behavior of
 * logical and of all of them)
 * 
 * actionf_p1, actionf_v and actionf_p2 are defined as these:
 * 
 * typedef  void (*actionf_v)();
 * typedef  void (*actionf_p1)( void* );
 * typedef  void (*actionf_p2)( void*, void* );
 * 
 * As you can see, they are pointers, so they all occupy the same space
 * in the union: the length of the memory pointer.
 * 
 * Effectively, this means that you can write to any of the three fields
 * the pointer to the function correspoding to the field, and
 * it will completely overwrite any other function assigned in other
 * two fields. Even more: the other fields will have the same pointer,
 * just with wrong type.
 * 
 * In Mocha Doom, this were addressed differently. A special helper enum
 * was created to hold possible names of the functions, and they were checked
 * by name, not by equality of the objects (object == object if point the same)
 * assigned to one of three fields. But, not understanding the true nature
 * of C's unions, in Mocha Doom all three fields were preserved and threated
 * like they can hold some different information at the same time.
 * 
 * I present hereby the solution that will both simplify the definition
 * and usage of the action functions, and provide a way to achieve the
 * exact same behavior as would be in C: if you assign the function,
 * you will replace the old one (virtually, "all the three fields")
 * and you can call any function with 0 to 2 arguments.
 * 
 * Also to store the functions in the same place where we declare them,
 * an Command pattern is implemented, requiring the function caller
 * to provide himself or any sufficient class that implements the Client
 * contract to provide the information needed for holding the state
 * of action functions.
 * 
 * - Good Sign 2017/04/28
 * 
 * Thinkers can either have one parameter of type (mobj_t),
 * Or otherwise be sector specials, flickering lights etc.
 * Those are atypical and need special handling.
 */
public enum ActiveStates {
    NOP((ThinkerConsumer) (a,b) -> {}, ThinkerConsumer.class),
    A_Light0((PlayerSpriteConsumer) ActionFunctions::A_Light0, PlayerSpriteConsumer.class),
    A_WeaponReady((PlayerSpriteConsumer) ActionFunctions::A_WeaponReady, PlayerSpriteConsumer.class),
    A_Lower((PlayerSpriteConsumer) ActionFunctions::A_Lower, PlayerSpriteConsumer.class),
    A_Raise((PlayerSpriteConsumer) ActionFunctions::A_Raise, PlayerSpriteConsumer.class),
    A_Punch((PlayerSpriteConsumer) ActionFunctions::A_Punch, PlayerSpriteConsumer.class),
    A_ReFire((PlayerSpriteConsumer) ActionFunctions::A_ReFire, PlayerSpriteConsumer.class),
    A_FirePistol((PlayerSpriteConsumer) ActionFunctions::A_FirePistol, PlayerSpriteConsumer.class),
    A_Light1((PlayerSpriteConsumer) ActionFunctions::A_Light1, PlayerSpriteConsumer.class),
    A_FireShotgun((PlayerSpriteConsumer) ActionFunctions::A_FireShotgun, PlayerSpriteConsumer.class),
    A_Light2((PlayerSpriteConsumer) ActionFunctions::A_Light2, PlayerSpriteConsumer.class),
    A_FireShotgun2((PlayerSpriteConsumer) ActionFunctions::A_FireShotgun2, PlayerSpriteConsumer.class),
    A_CheckReload((PlayerSpriteConsumer) ActionFunctions::A_CheckReload, PlayerSpriteConsumer.class),
    A_OpenShotgun2((PlayerSpriteConsumer) ActionFunctions::A_OpenShotgun2, PlayerSpriteConsumer.class),
    A_LoadShotgun2((PlayerSpriteConsumer) ActionFunctions::A_LoadShotgun2, PlayerSpriteConsumer.class),
    A_CloseShotgun2((PlayerSpriteConsumer) ActionFunctions::A_CloseShotgun2, PlayerSpriteConsumer.class),
    A_FireCGun((PlayerSpriteConsumer) ActionFunctions::A_FireCGun, PlayerSpriteConsumer.class),
    A_GunFlash((PlayerSpriteConsumer) ActionFunctions::A_GunFlash, PlayerSpriteConsumer.class),
    A_FireMissile((PlayerSpriteConsumer) ActionFunctions::A_FireMissile, PlayerSpriteConsumer.class),
    A_Saw((PlayerSpriteConsumer) ActionFunctions::A_Saw, PlayerSpriteConsumer.class),
    A_FirePlasma((PlayerSpriteConsumer) ActionFunctions::A_FirePlasma, PlayerSpriteConsumer.class),
    A_BFGsound((PlayerSpriteConsumer) ActionFunctions::A_BFGsound, PlayerSpriteConsumer.class),
    A_FireBFG((PlayerSpriteConsumer) ActionFunctions::A_FireBFG, PlayerSpriteConsumer.class),
    A_BFGSpray((MobjConsumer) ActionFunctions::A_BFGSpray, MobjConsumer.class),
    A_Explode((MobjConsumer) ActionFunctions::A_Explode, MobjConsumer.class),
    A_Pain((MobjConsumer) ActionFunctions::A_Pain, MobjConsumer.class),
    A_PlayerScream((MobjConsumer) ActionFunctions::A_PlayerScream, MobjConsumer.class),
    A_Fall((MobjConsumer) ActionFunctions::A_Fall, MobjConsumer.class),
    A_XScream((MobjConsumer) ActionFunctions::A_XScream, MobjConsumer.class),
    A_Look((MobjConsumer) ActionFunctions::A_Look, MobjConsumer.class),
    A_Chase((MobjConsumer) ActionFunctions::A_Chase, MobjConsumer.class),
    A_FaceTarget((MobjConsumer) ActionFunctions::A_FaceTarget, MobjConsumer.class),
    A_PosAttack((MobjConsumer) ActionFunctions::A_PosAttack, MobjConsumer.class),
    A_Scream((MobjConsumer) ActionFunctions::A_Scream, MobjConsumer.class),
    A_SPosAttack((MobjConsumer) ActionFunctions::A_SPosAttack, MobjConsumer.class),
    A_VileChase((MobjConsumer) ActionFunctions::A_VileChase, MobjConsumer.class),
    A_VileStart((MobjConsumer) ActionFunctions::A_VileStart, MobjConsumer.class),
    A_VileTarget((MobjConsumer) ActionFunctions::A_VileTarget, MobjConsumer.class),
    A_VileAttack((MobjConsumer) ActionFunctions::A_VileAttack, MobjConsumer.class),
    A_StartFire((MobjConsumer) ActionFunctions::A_StartFire, MobjConsumer.class),
    A_Fire((MobjConsumer) ActionFunctions::A_Fire, MobjConsumer.class),
    A_FireCrackle((MobjConsumer) ActionFunctions::A_FireCrackle, MobjConsumer.class),
    A_Tracer((MobjConsumer) ActionFunctions::A_Tracer, MobjConsumer.class),
    A_SkelWhoosh((MobjConsumer) ActionFunctions::A_SkelWhoosh, MobjConsumer.class),
    A_SkelFist((MobjConsumer) ActionFunctions::A_SkelFist, MobjConsumer.class),
    A_SkelMissile((MobjConsumer) ActionFunctions::A_SkelMissile, MobjConsumer.class),
    A_FatRaise((MobjConsumer) ActionFunctions::A_FatRaise, MobjConsumer.class),
    A_FatAttack1((MobjConsumer) ActionFunctions::A_FatAttack1, MobjConsumer.class),
    A_FatAttack2((MobjConsumer) ActionFunctions::A_FatAttack2, MobjConsumer.class),
    A_FatAttack3((MobjConsumer) ActionFunctions::A_FatAttack3, MobjConsumer.class),
    A_BossDeath((MobjConsumer) ActionFunctions::A_BossDeath, MobjConsumer.class),
    A_CPosAttack((MobjConsumer) ActionFunctions::A_CPosAttack, MobjConsumer.class),
    A_CPosRefire((MobjConsumer) ActionFunctions::A_CPosRefire, MobjConsumer.class),
    A_TroopAttack((MobjConsumer) ActionFunctions::A_TroopAttack, MobjConsumer.class),
    A_SargAttack((MobjConsumer) ActionFunctions::A_SargAttack, MobjConsumer.class),
    A_HeadAttack((MobjConsumer) ActionFunctions::A_HeadAttack, MobjConsumer.class),
    A_BruisAttack((MobjConsumer) ActionFunctions::A_BruisAttack, MobjConsumer.class),
    A_SkullAttack((MobjConsumer) ActionFunctions::A_SkullAttack, MobjConsumer.class),
    A_Metal((MobjConsumer) ActionFunctions::A_Metal, MobjConsumer.class),
    A_SpidRefire((MobjConsumer) ActionFunctions::A_SpidRefire, MobjConsumer.class),
    A_BabyMetal((MobjConsumer) ActionFunctions::A_BabyMetal, MobjConsumer.class),
    A_BspiAttack((MobjConsumer) ActionFunctions::A_BspiAttack, MobjConsumer.class),
    A_Hoof((MobjConsumer) ActionFunctions::A_Hoof, MobjConsumer.class),
    A_CyberAttack((MobjConsumer) ActionFunctions::A_CyberAttack, MobjConsumer.class),
    A_PainAttack((MobjConsumer) ActionFunctions::A_PainAttack, MobjConsumer.class),
    A_PainDie((MobjConsumer) ActionFunctions::A_PainDie, MobjConsumer.class),
    A_KeenDie((MobjConsumer) ActionFunctions::A_KeenDie, MobjConsumer.class),
    A_BrainPain((MobjConsumer) ActionFunctions::A_BrainPain, MobjConsumer.class),
    A_BrainScream((MobjConsumer) ActionFunctions::A_BrainScream, MobjConsumer.class),
    A_BrainDie((MobjConsumer) ActionFunctions::A_BrainDie, MobjConsumer.class),
    A_BrainAwake((MobjConsumer) ActionFunctions::A_BrainAwake, MobjConsumer.class),
    A_BrainSpit((MobjConsumer) ActionFunctions::A_BrainSpit, MobjConsumer.class),
    A_SpawnSound((MobjConsumer) ActionFunctions::A_SpawnSound, MobjConsumer.class),
    A_SpawnFly((MobjConsumer) ActionFunctions::A_SpawnFly, MobjConsumer.class),
    A_BrainExplode((MobjConsumer) ActionFunctions::A_BrainExplode, MobjConsumer.class),
    P_MobjThinker((MobjConsumer) ActionFunctions::P_MobjThinker, MobjConsumer.class),
    T_FireFlicker((ThinkerConsumer) ActionFunctions::T_FireFlicker, ThinkerConsumer.class),
    T_LightFlash((ThinkerConsumer) ActionFunctions::T_LightFlash, ThinkerConsumer.class),
    T_StrobeFlash((ThinkerConsumer) ActionFunctions::T_StrobeFlash, ThinkerConsumer.class),
    T_Glow((ThinkerConsumer) ActionFunctions::T_Glow, ThinkerConsumer.class),
    T_MoveCeiling((ThinkerConsumer) ActionFunctions::T_MoveCeiling, ThinkerConsumer.class),
    T_MoveFloor((ThinkerConsumer) ActionFunctions::T_MoveFloor, ThinkerConsumer.class),
    T_VerticalDoor((ThinkerConsumer) ActionFunctions::T_VerticalDoor, ThinkerConsumer.class),
    T_PlatRaise((ThinkerConsumer) ActionFunctions::T_PlatRaise, ThinkerConsumer.class),
    T_SlidingDoor((ThinkerConsumer) Thinkers::T_SlidingDoor, ThinkerConsumer.class);
    
    private final static Logger LOGGER = LoggersKt.getLogger(ActiveStates.class.getName());
    
    private final ParamClass<?> actionFunction;
    private final Class<? extends ParamClass<?>> paramType;

    ActiveStates(final ParamClass<?> actionFunction, final Class<? extends ParamClass<?>> paramType) {
        this.actionFunction = actionFunction;
        this.paramType = paramType;
    }

    @actionf_p1
    @D_Think.C(actionf_t.acp1)
    public interface MobjConsumer extends ParamClass<MobjConsumer> {
    	void accept(ActionFunctions a, mobj_t m);
    }
    
    @actionf_v
    @D_Think.C(actionf_t.acv)
    public interface ThinkerConsumer extends ParamClass<ThinkerConsumer> {
    	void accept(ActionFunctions a, thinker_t t);
    }
    
    @actionf_p2
    @D_Think.C(actionf_t.acp2)
    public interface PlayerSpriteConsumer extends ParamClass<PlayerSpriteConsumer> {
    	void accept(ActionFunctions a, player_t p, pspdef_t s);
    }
    
    private interface ParamClass<T extends ParamClass<T>> {}
    
    public boolean isParamType(final Class<?> paramType) {
        return this.paramType == paramType;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends ParamClass<T>> T fun(final Class<T> paramType) {
        if (this.paramType != paramType) {
            LOGGER.log(Level.WARNING, "Wrong paramType for state: {0}", this);
            return null;
        }
        
        // don't believe, it's checked
        return (T) this.actionFunction;
    }
}
