package com.vismaad.naad.player.service;

/**
 * Created by DELL on 1/29/2018.
 */
public interface MediaPlayerState {

    String Action_Previous = "domain.com.player.Previous";
    String Action_Pause_play = "domain.com.player.PausePlay";
    String Action_Stop = "dom.com.player.Stop";
    String Action_Next = "domain.com.player.Next";
    int PREVIOUS = 5;
    int PAUSE = 10;
    int STOP = 15;
    int NEXT = 20;
    int NOTIF_ID = 101;
    String SHOW_SHABAD = "SHOW_SHABAD";
    String RAAGI_NAME = "RAAGI_NAME";
    String SHABAD_LINKS = "SHABAD_LINKS";
    String SHABAD_TITLES = "SHABAD_TITLES";
    String ORIGINAL_SHABAD = "ORIGINAL_SHABAD";


    String RADIO = "radio";
    String SHABAD = "SHABAD";
    String shabad_list = "shabad_list";
    String SHABAD_DURATION = "SHABAD_DURATION";

    String Action_Play = "Action_Play";
    String updateUI = "updateUI";


    String SWIPE_TO_DISMISS = "SWIPE_TO_DISMISS";
    String RAAGI_IMAGE = "RAAGI_IMAGE";
}