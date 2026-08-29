package K.Othermod.NH;

import K.KMod;
import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.style.TextureRegionDrawable;
import arc.util.Strings;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;

public class CustomProgressBarEntry {
    private Object iconValue;
    private TextureRegion icon = Icon.chartBar.getRegion();
    private float current;
    private float maximum = 1f;
    private boolean completed;

    public void update(Object iconValue, float current, float maximum) {
        if (this.iconValue != iconValue) {
            this.iconValue = iconValue;
            icon = resolveIcon(iconValue);
        }

        this.current = Float.isFinite(current) ? current : 0f;
        this.maximum = Float.isFinite(maximum) ? maximum : 0f;
    }

    public void complete() {
        completed = true;
    }

    public boolean completed() {
        return completed;
    }

    private static String format(float value) {
        return Strings.fixed(value, Mathf.equal(value, Mathf.round(value)) ? 0 : 1);
    }

    private static TextureRegion resolveIcon(Object value) {
        if (value instanceof UnlockableContent content && content.uiIcon != null) return content.uiIcon;
        if (value instanceof TextureRegion region) return region;
        if (value instanceof TextureRegionDrawable drawable) return drawable.getRegion();

        if (value instanceof String string) {
            String name = string.startsWith("@") ? string.substring(1) : string;
            TextureRegionDrawable iconDrawable = Icon.icons.get(name);
            if (iconDrawable != null) return iconDrawable.getRegion();

            TextureRegion region = Core.atlas.find(name);
            if (region.found()) return region;

            region = Core.atlas.find(KMod.name(name));
            if (region.found()) return region;
        }

        return Icon.chartBar.getRegion();
    }
}
