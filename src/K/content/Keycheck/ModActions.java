package K.content.Keycheck;

import arc.scene.ui.Button;

public class ModActions {
    private static Button btn1 = null;
    private static Button btn2 = null;
    private static Button btn3 = null;
    private static Button btn4 = null;

    private static boolean pressed1 = false;
    private static boolean pressed2 = false;
    private static boolean pressed3 = false;
    private static boolean pressed4 = false;

    private static boolean prev1 = false;
    private static boolean prev2 = false;
    private static boolean prev3 = false;
    private static boolean prev4 = false;

    public static void setButton1(Button b) { btn1 = b; }
    public static void setButton2(Button b) { btn2 = b; }
    public static void setButton3(Button b) { btn3 = b; }
    public static void setButton4(Button b) { btn4 = b; }

    public static boolean isButton1Pressed() { return pressed1; }
    public static boolean isButton2Pressed() { return pressed2; }
    public static boolean isButton3Pressed() { return pressed3; }
    public static boolean isButton4Pressed() { return pressed4; }

    public static void updateAllButtonStates() {
        if (btn1 != null) {
            pressed1 = btn1.isPressed();
            if (pressed1 != prev1) {
                KeybindRody.f2d();
                prev1 = pressed1;
            }
        }
        if (btn2 != null) {
            pressed2 = btn2.isPressed();
            if (pressed2 != prev2) {
                KeybindRody.f3d();
                prev2 = pressed2;
            }
        }
        if (btn3 != null) {
            pressed3 = btn3.isPressed();
            if (pressed3 != prev3) {
                KeybindRody.f4d();
                prev3 = pressed3;
            }
        }
        if (btn4 != null) {
            pressed4 = btn4.isPressed();
            if (pressed4 != prev4) {
                KeybindRody.f5d();
                prev4 = pressed4;
            }
        }
    }

    // 在 ModActions.java 中添加
    public static void setButtonsVisible(boolean visible) {
        if (btn1 != null) btn1.visible=visible;
        if (btn2 != null) btn2.visible=visible;
        if (btn3 != null) btn3.visible=visible;
        if (btn4 != null) btn4.visible=visible;
    }
}