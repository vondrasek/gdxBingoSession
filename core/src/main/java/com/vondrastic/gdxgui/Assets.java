/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.vondrastic.gdxgui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Assets {
	public static final int screenwidth = 1280;
	public static final int screenheight = 800; //736;
	// Fonts
	public static BitmapFont font32;
	public static BitmapFont font24;
	public static BitmapFont font18;
	// Background. 
	private static TextureAtlas atlas;
	public static boolean loaded = false;
	public static Texture background;
	public static TextureRegion backgroundRegion;
	
	// 90 Numbers
	//public static Texture card90_A;
	//public static List<TextureRegion> card90_A_Region = new ArrayList<TextureRegion>();
	
	public static List<TextureRegion> cardnbr_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> card_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> dauber_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> flashboard_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> pattern_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> colorballs_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> overlayballs_region = new ArrayList<TextureRegion>();
	public static List<TextureRegion> oneaway_region = new ArrayList<TextureRegion>();
	public static TextureRegion guiBarRegion;
	
	public static TextureRegion keybtnUP_region;
	public static TextureRegion keybtnDN_region;
	public static TextureRegion menu_exit;
	// Menu Icons
	public static List<TextureRegion> menu_volume_on = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_volume_off = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_nav_forward = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_nav_back = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_nav_next = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_nav_previous = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_nav_up = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_nav_down = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_schedulelist = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_dauberlist = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_keypad = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_flashboard = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_accept = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_cancel = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_cardview_more = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_cardview_less = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_zoom_in = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_zoom_out = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_help = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_settings = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_admin = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_undo = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_alert = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_edit = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_save = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_add = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_discard = new ArrayList<TextureRegion>();
	public static List<TextureRegion> menu_pattern = new ArrayList<TextureRegion>();
	public static List<TextureRegion> battery_info = new ArrayList<TextureRegion>();
	public static List<TextureRegion> bingoballs = new ArrayList<TextureRegion>();
	public static TextureRegion menu_bg;
	
	public static Array<AtlasRegion> dauber_penguin_region;
	public static Array<AtlasRegion> dauber_bull_region;
	public static Array<AtlasRegion> dauber_duck_region;
	public static Array<AtlasRegion> dauber_tomato_region;
	public static Array<AtlasRegion> dauber_moonphase_region;
	public static Array<AtlasRegion> dauber_matrix_region;
	
	public static List<Sound> winner_sounds = new ArrayList<Sound>();
	
	public static TextureRegion guibtn;
	public static Texture scratch;
	public static Animation scratch_animation;
	public static Music music;
	public static Sound daubSound;
	public static Sound oneawaySound;
	public static Sound winnerSound;
	public static Sound swipeSound;
	public static Sound alert1Sound;
	public static Sound clickSound;
	
	public static Sound coinSound;
	// CKB Sounds
	public static Sound[] ballcallSound = new Sound[75]; // 
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		
		atlas = new TextureAtlas(Gdx.files.internal("data/gui.txt"));
		guiBarRegion = atlas.findRegion("bar_black");
		
		atlas = new TextureAtlas(Gdx.files.internal("data/star_rotate.txt"));
		
		oneaway_region.clear();
		for(int i=1; i<16; i++){
			oneaway_region.add(atlas.findRegion("Star",i));
		}
		
		atlas = new TextureAtlas(Gdx.files.internal("data/CardNbrs.txt"));
		
		cardnbr_region.clear();
		for(int i=1; i<76; i++){
			cardnbr_region.add(atlas.findRegion("n",i));
		}
		
		atlas = new TextureAtlas(Gdx.files.internal("data/battery.txt"));
		battery_info.clear();
		battery_info.add(atlas.findRegion("battery"));
		battery_info.add(atlas.findRegion("batteryFiller"));
		battery_info.add(atlas.findRegion("batteryCharge"));
		
		atlas = new TextureAtlas(Gdx.files.internal("data/bingoballs.txt"));
		
		bingoballs.clear();
		bingoballs.add(atlas.findRegion("B"));
		bingoballs.add(atlas.findRegion("I"));
		bingoballs.add(atlas.findRegion("N"));
		bingoballs.add(atlas.findRegion("G"));
		bingoballs.add(atlas.findRegion("O"));
		
		// --------------------------------------------------------------------------
		// MENU ASSETS
		menu_bg = new TextureRegion(loadTexture("data/menu_bg.png"));
		atlas = new TextureAtlas(Gdx.files.internal("data/menuicons_dark.txt"));
		
		menu_volume_on.clear();
		menu_volume_on.add(atlas.findRegion("10_device_access_volume_on"));
		menu_volume_off.clear();
		menu_volume_off.add(atlas.findRegion("10_device_access_volume_muted"));
		menu_nav_forward.clear();
		menu_nav_forward.add(atlas.findRegion("1_navigation_forward"));
		menu_nav_back.clear();
		menu_nav_back.add(atlas.findRegion("1_navigation_back"));
		menu_nav_next.clear();
		menu_nav_next.add(atlas.findRegion("1_navigation_next_item"));
		menu_nav_previous.clear();
		menu_nav_previous.add(atlas.findRegion("1_navigation_previous_item"));
		menu_nav_up.clear();
		menu_nav_up.add(atlas.findRegion("1_navigation_collapse"));
		menu_nav_down.clear();
		menu_nav_down.add(atlas.findRegion("1_navigation_expand"));
		menu_schedulelist.clear();
		menu_schedulelist.add(atlas.findRegion("4_collections_view_as_list"));
		menu_keypad.clear();
		menu_keypad.add(atlas.findRegion("10_device_access_dial_pad"));
		menu_flashboard.clear();
		menu_flashboard.add(atlas.findRegion("12_hardware_keyboard"));
		menu_accept.clear();
		menu_accept.add(atlas.findRegion("1_navigation_accept"));
		menu_cancel.clear();
		menu_cancel.add(atlas.findRegion("1_navigation_cancel"));
		menu_cardview_more.clear();
		menu_cardview_more.add(atlas.findRegion("4_collections_view_as_grid"));
		menu_cardview_less.clear();
		menu_cardview_less.add(atlas.findRegion("5_content_select_all"));
		menu_zoom_in.clear();
		menu_zoom_in.add(atlas.findRegion("9_av_return_from_full_screen"));
		menu_zoom_out.clear();
		menu_zoom_out.add(atlas.findRegion("9_av_full_screen"));
		menu_help.clear();
		menu_help.add(atlas.findRegion("2_action_help"));
		menu_settings.clear();
		menu_settings.add(atlas.findRegion("2_action_settings"));
		menu_admin.clear();
		menu_admin.add(atlas.findRegion("2_action_settings"));
		menu_undo.clear();
		menu_undo.add(atlas.findRegion("6_social_reply"));
		menu_alert.clear();
		menu_alert.add(atlas.findRegion("11_alerts_and_states_warning"));
		menu_edit.clear();
		menu_edit.add(atlas.findRegion("5_content_edit"));
		menu_save.clear();
		menu_save.add(atlas.findRegion("5_content_save"));
		menu_add.clear();
		menu_add.add(atlas.findRegion("5_content_new"));
		menu_discard.clear();
		menu_discard.add(atlas.findRegion("5_content_discard"));
		menu_dauberlist.clear();
		menu_dauberlist.add(atlas.findRegion("5_content_picture"));
		menu_pattern.clear();
		menu_pattern.add(atlas.findRegion("5_content_select_all"));
		//  Menu Light 
		atlas = new TextureAtlas(Gdx.files.internal("data/menuicons_light.txt"));
		menu_exit = atlas.findRegion("2_action_exit");
		menu_volume_on.add(atlas.findRegion("10_device_access_volume_on"));
		menu_volume_off.add(atlas.findRegion("10_device_access_volume_muted"));
		menu_nav_forward.add(atlas.findRegion("1_navigation_forward"));
		menu_nav_back.add(atlas.findRegion("1_navigation_back"));
		menu_nav_next.add(atlas.findRegion("1_navigation_next_item"));
		menu_nav_previous.add(atlas.findRegion("1_navigation_previous_item"));
		menu_nav_up.add(atlas.findRegion("1_navigation_collapse"));
		menu_nav_down.add(atlas.findRegion("1_navigation_expand"));
		menu_schedulelist.add(atlas.findRegion("4_collections_view_as_list"));
		menu_keypad.add(atlas.findRegion("10_device_access_dial_pad"));
		menu_flashboard.add(atlas.findRegion("12_hardware_keyboard"));
		menu_accept.add(atlas.findRegion("1_navigation_accept"));
		menu_cancel.add(atlas.findRegion("1_navigation_cancel"));
		menu_cardview_more.add(atlas.findRegion("4_collections_view_as_grid"));
		menu_cardview_less.add(atlas.findRegion("5_content_select_all"));
		menu_zoom_in.add(atlas.findRegion("9_av_return_from_full_screen"));
		menu_zoom_out.add(atlas.findRegion("9_av_full_screen"));
		menu_help.add(atlas.findRegion("2_action_help"));
		menu_settings.add(atlas.findRegion("2_action_settings"));
		menu_admin.add(atlas.findRegion("2_action_settings"));
		menu_undo.add(atlas.findRegion("6_social_reply"));
		menu_alert.add(atlas.findRegion("11_alerts_and_states_warning"));
		menu_edit.add(atlas.findRegion("5_content_edit"));
		menu_save.add(atlas.findRegion("5_content_save"));
		menu_add.add(atlas.findRegion("5_content_new"));
		menu_discard.add(atlas.findRegion("5_content_discard"));
		menu_dauberlist.add(atlas.findRegion("5_content_picture"));
		menu_pattern.add(atlas.findRegion("5_content_select_all"));
		// --------------------------------------------------------------------------
		//Sprite sprite = atlas.createSprite("otherimagename");
		atlas = new TextureAtlas(Gdx.files.internal("data/colorballs.txt"));
		overlayballs_region.clear();
		for(int i=1; i<5; i++){
			overlayballs_region.add(atlas.findRegion("Overlay",i));
		}
		colorballs_region.clear();
		colorballs_region.add(atlas.findRegion("orange"));
		colorballs_region.add(atlas.findRegion("red"));
		colorballs_region.add(atlas.findRegion("green"));
		colorballs_region.add(atlas.findRegion("blue"));
		colorballs_region.add(atlas.findRegion("violet"));
		colorballs_region.add(atlas.findRegion("grey"));
		colorballs_region.add(atlas.findRegion("teal"));
		colorballs_region.add(atlas.findRegion("yellow"));
		
		backgroundRegion = new TextureRegion(loadTexture("data/bg.png"));
		keybtnDN_region = new TextureRegion(loadTexture("data/keybtn_dn.png"));
		keybtnUP_region = new TextureRegion(loadTexture("data/keybtn_up.png"));
		
		// Font for Game
		font32 = new BitmapFont(Gdx.files.internal("fonts/ComicSans32.fnt"), Gdx.files.internal("fonts/ComicSans32.png"), false);
		font24 = new BitmapFont(Gdx.files.internal("fonts/ComicSans24.fnt"), Gdx.files.internal("fonts/ComicSans24.png"), false);
		font18 = new BitmapFont(Gdx.files.internal("fonts/ComicSans18.fnt"), Gdx.files.internal("fonts/ComicSans18.png"), false);
		
		atlas = new TextureAtlas(Gdx.files.internal("data/Cards.txt"));
		card_region.clear();
		for(int i=1; i<15; i++){
			card_region.add(atlas.findRegion("card",i));
		}
		
		atlas = new TextureAtlas(Gdx.files.internal("data/daubers.txt"));
		dauber_region.clear();
		for(int i=1; i<80; i++){
			dauber_region.add(atlas.findRegion("d",i));
		}
		
		atlas = new TextureAtlas(Gdx.files.internal("data/flashboard.txt"));
		//Sprite.split(texture, tileWidth, tileHeight)
		flashboard_region.clear();
		flashboard_region.add(atlas.findRegion("flashOff"));
		flashboard_region.add(atlas.findRegion("flashGreen"));
		flashboard_region.add(atlas.findRegion("flashOneAway"));
		flashboard_region.add(atlas.findRegion("flashRed"));
		
		
		atlas = new TextureAtlas(Gdx.files.internal("data/Pattern.txt"));
		pattern_region.clear();
		pattern_region.add(atlas.findRegion("patternbox"));
		pattern_region.add(atlas.findRegion("pat_on"));
		
		atlas = new TextureAtlas(Gdx.files.internal("data/DauberAnimte.txt"));
		
		//dauber_penguin_region = atlas.findRegions("penguin");
		//dauber_moonphase_region = atlas.findRegions("MoonPhase");
		//dauber_bull_region = atlas.findRegions("bull");
		//dauber_tomato_region = atlas.findRegions("tomato");
		//dauber_duck_region = atlas.findRegions("duck");
		//dauber_matrix_region = atlas.findRegions("matrix");
		
		// SOUNDS 
		for(int i = 0; i< 75; i++){
			ballcallSound[i] = Gdx.audio.newSound(Gdx.files.internal("audio/Call/" + String.valueOf(i+1) + ".wav"));
		}
		
		oneawaySound = Gdx.audio.newSound(Gdx.files.internal("audio/oneaway.wav"));
		winnerSound = Gdx.audio.newSound(Gdx.files.internal("audio/winner.wav"));
		swipeSound = Gdx.audio.newSound(Gdx.files.internal("audio/swipe.wav"));
		alert1Sound = Gdx.audio.newSound(Gdx.files.internal("audio/alert_1.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.wav"));
		
		// Winner Sound
		// Alert Sound
		// Dauber Sound
		// 
		//music = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/music.mp3"));
		//music.setLooping(true);
		//music.setVolume(0.5f);
		
		//font.setColor(Color.WHITE);
		//font.setScale(2);
		// Background
		
		// BingoCards
		// Pattern
    	// Gems
		
		//login gui
		
		//Animations
		//scratch = loadTexture("assets/images/bingo/Scratch.png");
		//scratch_animation = new Animation(0.08f, 
		//		new TextureRegion(scratch, 0, 0, 128, 128), new TextureRegion(scratch, 128, 0, 128, 128), new TextureRegion(scratch, 256, 0, 128, 128), new TextureRegion(scratch, 384, 0, 128, 128),
		//		new TextureRegion(scratch, 0, 128, 128, 128), new TextureRegion(scratch, 128, 128, 128, 128), new TextureRegion(scratch, 256, 128, 128, 128), new TextureRegion(scratch, 384, 128, 128, 128),
		//		new TextureRegion(scratch, 0, 256, 128, 128), new TextureRegion(scratch, 128, 256, 128, 128), new TextureRegion(scratch, 256, 256, 128, 128), new TextureRegion(scratch, 384, 256, 128, 128),
		//		new TextureRegion(scratch, 0, 384, 128, 128), new TextureRegion(scratch, 128, 384, 128, 128), new TextureRegion(scratch, 256, 384, 128, 128), new TextureRegion(scratch, 384, 384, 128, 128));
				
		// Sound Area
		//music = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/music.mp3"));
		//music.setLooping(true);
		//music.setVolume(0.5f);
		// if (Settings.soundEnabled) music.play();
		//music.play();
		
		//clickSound = Gdx.audio.newSound(Gdx.files.internal("assets/audio/click.ogg"));
		loaded = true;
	}

	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) sound.play(0.5f);
	}
	
}
