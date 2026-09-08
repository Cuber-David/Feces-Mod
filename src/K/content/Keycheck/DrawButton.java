package K.content.Keycheck;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.event.InputEvent;
import arc.scene.event.ClickListener;
import arc.scene.ui.Button;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.ui.Styles;

public class DrawButton {
    public static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        if (Core.scene == null) {
            Events.on(EventType.ClientLoadEvent.class, e -> init());
            return;
        }
        createButtons();
        Events.run(EventType.Trigger.update, () -> {
            // 更新所有按钮的状态
            ModActions.updateAllButtonStates();

            // 检测每个按钮是否被按住，并输出日志
            if (ModActions.isButton1Pressed()) {
                KeybindRoody.f2();
            }
            if (ModActions.isButton2Pressed()) {
                KeybindRoody.f3();
            }
            if (ModActions.isButton3Pressed()) {
                KeybindRoody.f4();
            }
            if (ModActions.isButton4Pressed()) {
                KeybindRoody.f5();
            }
        });
    }

    private static void createButtons() {
        try {
            Core.scene.table(Styles.none, root -> {
                root.top().marginBottom(200);
                root.margin(10f, 20f, 100f, 0f);

                root.table(Styles.none, buttons -> {
                    buttons.margin(6f);
                    buttons.defaults().size(50f, 50f).pad(2f);

                    // 创建四个按钮
                    Button b1 = createButton("F2", Color.orange);
                    Button b2 = createButton("F3", Color.cyan);
                    Button b3 = createButton("F4", Color.purple);
                    Button b4 = createButton("F5", Color.green);

                    // 保存引用
                    ModActions.setButton1(b1);
                    ModActions.setButton2(b2);
                    ModActions.setButton3(b3);
                    ModActions.setButton4(b4);

                    // 添加到表格
                    buttons.add(b1);
                    buttons.add(b2);
                    buttons.add(b3);
                    buttons.add(b4);
                });
            });
            initialized = true;
        } catch (Throwable t) {
            Log.err("按钮创建失败: " + t);
        }
    }

    // 辅助方法：创建一个按钮
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