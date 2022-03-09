package doom

@Target
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class SourceCode {
    enum class AM_Map {
        AM_Responder, AM_Ticker, AM_Drawer, AM_Stop;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: AM_Map)
    }

    enum class D_Main {
        D_DoomLoop, D_ProcessEvents;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: D_Main)
    }

    enum class F_Finale {
        F_Responder, F_Ticker, F_Drawer, F_StartFinale;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: F_Finale)
    }

    enum class G_Game {
        G_BuildTiccmd, G_DoCompleted, G_DoReborn, G_DoLoadLevel, G_DoSaveGame, G_DoPlayDemo, G_PlayerFinishLevel, G_DoNewGame, G_PlayerReborn, G_CheckSpot, G_DeathMatchSpawnPlayer, G_InitNew, G_DeferedInitNew, G_DeferedPlayDemo, G_LoadGame, G_DoLoadGame, G_SaveGame, G_RecordDemo, G_BeginRecording, G_PlayDemo, G_TimeDemo, G_CheckDemoStatus, G_ExitLevel, G_SecretExitLevel, G_WorldDone, G_Ticker, G_Responder, G_ScreenShot;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: G_Game)
    }

    enum class HU_Lib {
        HUlib_init, HUlib_clearTextLine, HUlib_initTextLine, HUlib_addCharToTextLine, HUlib_delCharFromTextLine, HUlib_drawTextLine, HUlib_eraseTextLine, HUlib_initSText, HUlib_addLineToSText, HUlib_addMessageToSText, HUlib_drawSText, HUlib_eraseSText, HUlib_initIText, HUlib_delCharFromIText, HUlib_eraseLineFromIText, HUlib_resetIText, HUlib_addPrefixToIText, HUlib_keyInIText, HUlib_drawIText, HUlib_eraseIText;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: HU_Lib)
    }

    enum class HU_Stuff {
        HU_Init, HU_Start, HU_Responder, HU_Ticker, HU_Drawer, HU_queueChatChar, HU_dequeueChatChar, HU_Erase;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: HU_Stuff)
    }

    enum class I_IBM {
        I_GetTime, I_WaitVBL, I_SetPalette, I_FinishUpdate, I_StartTic, I_InitNetwork, I_NetCmd;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: I_IBM)
    }

    enum class M_Argv {
        M_CheckParm;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: M_Argv)
    }

    enum class M_Menu {
        M_Responder, M_Ticker, M_Drawer, M_Init, M_StartControlPanel;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: M_Menu)
    }

    enum class M_Random {
        M_Random, P_Random, M_ClearRandom;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: SourceCode.M_Random)
    }

    enum class P_Doors {
        T_VerticalDoor, EV_VerticalDoor, EV_DoDoor, EV_DoLockedDoor, P_SpawnDoorCloseIn30, P_SpawnDoorRaiseIn5Mins, P_InitSlidingDoorFrames, P_FindSlidingDoorType, T_SlidingDoor, EV_SlidingDoor;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Doors)
    }

    enum class P_Map {
        P_CheckPosition, PIT_CheckThing, PIT_CheckLine, PIT_RadiusAttack, PIT_ChangeSector, PIT_StompThing, PTR_SlideTraverse, PTR_AimTraverse, PTR_ShootTraverse, PTR_UseTraverse;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Map)
    }

    enum class P_MapUtl {
        P_BlockThingsIterator, P_BlockLinesIterator, P_PathTraverse, P_UnsetThingPosition, P_SetThingPosition, PIT_AddLineIntercepts, PIT_AddThingIntercepts;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_MapUtl)
    }

    enum class P_Mobj {
        G_PlayerReborn, P_SpawnMapThing, P_SetMobjState, P_ExplodeMissile, P_XYMovement, P_ZMovement, P_NightmareRespawn, P_MobjThinker, P_SpawnMobj, P_RemoveMobj, P_RespawnSpecials, P_SpawnPlayer, P_SpawnPuff, P_SpawnBlood, P_CheckMissileSpawn, P_SpawnMissile, P_SpawnPlayerMissile;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Mobj)
    }

    enum class P_Enemy {
        PIT_VileCheck;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Enemy)
    }

    enum class P_Lights {
        T_FireFlicker, P_SpawnFireFlicker, T_LightFlash, P_SpawnLightFlash, T_StrobeFlash, P_SpawnStrobeFlash, EV_StartLightStrobing, EV_TurnTagLightsOff, EV_LightTurnOn, T_Glow, P_SpawnGlowingLight;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Lights)
    }

    enum class P_SaveG {
        P_ArchivePlayers, P_UnArchivePlayers, P_ArchiveWorld, P_UnArchiveWorld, P_ArchiveThinkers, P_UnArchiveThinkers, P_ArchiveSpecials, P_UnArchiveSpecials;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_SaveG)
    }

    enum class P_Setup {
        P_SetupLevel, P_LoadThings;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Setup)
    }

    enum class P_Spec {
        P_InitPicAnims, P_SpawnSpecials, P_UpdateSpecials, P_UseSpecialLine, P_ShootSpecialLine, P_CrossSpecialLine, P_PlayerInSpecialSector, twoSided, getSector, getSide, P_FindLowestFloorSurrounding, P_FindHighestFloorSurrounding, P_FindNextHighestFloor, P_FindLowestCeilingSurrounding, P_FindHighestCeilingSurrounding, P_FindSectorFromLineTag, P_FindMinSurroundingLight, getNextSector, EV_DoDonut, P_ChangeSwitchTexture, P_InitSwitchList, T_PlatRaise, EV_DoPlat, P_AddActivePlat, P_RemoveActivePlat, EV_StopPlat, P_ActivateInStasis, EV_DoCeiling, T_MoveCeiling, P_AddActiveCeiling, P_RemoveActiveCeiling, EV_CeilingCrushStop, P_ActivateInStasisCeiling, T_MovePlane, EV_BuildStairs, EV_DoFloor, T_MoveFloor, EV_Teleport;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Spec)
    }

    enum class P_Ceiling {
        EV_DoCeiling;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Ceiling)
    }

    enum class P_Tick {
        P_InitThinkers, P_RemoveThinker, P_AddThinker, P_AllocateThinker, P_RunThinkers, P_Ticker;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Tick)
    }

    enum class P_Pspr {
        P_SetPsprite, P_CalcSwing, P_BringUpWeapon, P_CheckAmmo, P_FireWeapon, P_DropWeapon, A_WeaponReady, A_ReFire, A_CheckReload, A_Lower, A_Raise, A_GunFlash, A_Punch, A_Saw, A_FireMissile, A_FireBFG, A_FirePlasma, P_BulletSlope, P_GunShot, A_FirePistol, A_FireShotgun, A_FireShotgun2, A_FireCGun, A_Light0, A_Light1, A_Light2, A_BFGSpray, A_BFGsound, P_SetupPsprites, P_MovePsprites;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: P_Pspr)
    }

    enum class R_Data {
        R_GetColumn, R_InitData, R_PrecacheLevel, R_FlatNumForName, R_TextureNumForName, R_CheckTextureNumForName;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: R_Data)
    }

    enum class R_Draw {
        R_FillBackScreen;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: R_Draw)
    }

    enum class R_Main {
        R_PointOnSide, R_PointOnSegSide, R_PointToAngle, R_PointToAngle2, R_PointToDist, R_ScaleFromGlobalAngle, R_PointInSubsector, R_AddPointToBox, R_RenderPlayerView, R_Init, R_SetViewSize;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: R_Main)
    }

    enum class ST_Stuff {
        ST_Responder, ST_Ticker, ST_Drawer, ST_Start, ST_Init;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: ST_Stuff)
    }

    enum class W_Wad {
        W_InitMultipleFiles, W_Reload, W_CheckNumForName, W_GetNumForName, W_LumpLength, W_ReadLump, W_CacheLumpNum, W_CacheLumpName;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: W_Wad)
    }

    enum class WI_Stuff {
        WI_initVariables, WI_loadData, WI_initDeathmatchStats, WI_initAnimatedBack, WI_initNetgameStats, WI_initStats, WI_Ticker, WI_Drawer, WI_Start;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: WI_Stuff)
    }

    interface D_Think {
        enum class actionf_t {
            acp1, acv, acp2
        }

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: actionf_t)
    }

    enum class Z_Zone {
        Z_Malloc;

        @MustBeDocumented
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class C(val value: Z_Zone)
    }

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Exact(
        val description: String = """Indicates that the method behaves exactly in vanilla way
 and can be skipped when traversing for compatibility"""
    )

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Compatible(
        vararg val value: String = [""],
        val description: String = """Indicates that the method can behave differently from vanilla way,
 but this behavior is reviewed and can be turned back to vanilla as an option.A value might be specivied with the equivalent vanilla code"""
    )

    enum class CauseOfDesyncProbability {
        LOW, MEDIUM, HIGH
    }

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Suspicious(
        val value: CauseOfDesyncProbability = CauseOfDesyncProbability.HIGH, val description: String = "Indicates that the method contains behavior totally different from vanilla, and by so should be considered suspicious in terms of compatibility"
    )

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD,
        AnnotationTarget.LOCAL_VARIABLE,
        AnnotationTarget.VALUE_PARAMETER
    )
    annotation class angle_t

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD,
        AnnotationTarget.LOCAL_VARIABLE,
        AnnotationTarget.VALUE_PARAMETER
    )
    annotation class fixed_t

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class actionf_p1

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class actionf_v

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class actionf_p2

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.VALUE_PARAMETER)
    annotation class thinker_t

    @MustBeDocumented
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.VALUE_PARAMETER)
    annotation class think_t
}