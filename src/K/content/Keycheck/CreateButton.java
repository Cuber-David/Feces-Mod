package K.content.Keycheck;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.Element;
import arc.scene.event.InputEvent;
import arc.scene.event.ClickListener;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.ui.Styles;

public class CreateButton {
    public static boolean initialized = false;

    // 保存根容器和每个组的引用
    private static Table buttonRoot = null;
    private static Element[] groups = new Element[4];

    public static void init() {
        if (initialized) return;
        if (Core.scene == null) {
            Events.on(EventType.ClientLoadEvent.class, e -> init());
            return;
        }
        createButtons();
        Events.run(EventType.Trigger.update, () -> {
            ModActions.updateAllButtonStates();

            if (ModActions.isButton1Pressed()) KeybindRody.f2();
            if (ModActions.isButton2Pressed()) KeybindRody.f3();
            if (ModActions.isButton3Pressed()) KeybindRody.f4();
            if (ModActions.isButton4Pressed()) KeybindRody.f5();
        });
    }

    private static void createButtons() {
        try {
            Core.scene.table(Styles.none, root -> {
                buttonRoot = root;
                root.left().margin(20, 10, 0, 0);
                root.margin(0f, 10f, 680f, 0f);

                root.table(Styles.none, buttons -> {
                    buttons.margin(6f);

                    Button b1 = createButton("F2", Color.red);
                    Button b2 = createButton("F3", Color.red);
                    Button b3 = createButton("F4", Color.red);
                    Button b4 = createButton("F5", Color.red);

                    ModActions.setButton1(b1);
                    ModActions.setButton2(b2);
                    ModActions.setButton3(b3);
                    ModActions.setButton4(b4);

                    // 保存每个组的引用
                    buttons.table(Styles.none, group -> {
                        group.add(b1).size(50f, 50f).row();
                        group.add(new CustomIconElement(0)).size(40f, 40f).padTop(3f).row();
                        groups[0] = group;
                    }).pad(2f);

                    buttons.table(Styles.none, group -> {
                        group.add(b2).size(50f, 50f).row();
                        group.add(new CustomIconElement(1)).size(40f, 40f).padTop(3f).row();
                        groups[1] = group;
                    }).pad(2f);

                    buttons.table(Styles.none, group -> {
                        group.add(b3).size(50f, 50f).row();
                        group.add(new CustomIconElement(2)).size(40f, 40f).padTop(3f).row();
                        groups[2] = group;
                    }).pad(2f);

                    buttons.table(Styles.none, group -> {
                        group.add(b4).size(50f, 50f).row();
                        group.add(new CustomIconElement(3)).size(40f, 40f).padTop(3f).row();
                        groups[3] = group;
                    }).pad(2f);
                });
            });
            initialized = true;
        } catch (Throwable t) {
            Log.err("按钮创建失败: " + t);
            t.printStackTrace();
        }
    }

    /**
     * 设置按钮和图标的可见性（直接设置 visible 变量）
     */
    public static void setVisible(boolean visible) {
        // 设置整个根容器的可见性
        if (buttonRoot != null) {
            buttonRoot.visible = visible;
        }

        // 同时设置每个组的可见性（确保图标也被隐藏）
        for (Element group : groups) {
            if (group != null) {
                group.visible = visible;
            }
        }
    }

    private static Button createButton(String labelText, Color color) {
        Button btn = new Button(Styles.defaulti);
        btn.label(() -> labelText);

        btn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                super.touchDown(event, x, y, pointer, button);
                btn.setColor(color);
                btn.setScale(0.85f);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                super.touchUp(event, x, y, pointer, button);
                btn.setColor(Color.white);
                btn.setScale(1f);
            }
        });
        return btn;
    }
}