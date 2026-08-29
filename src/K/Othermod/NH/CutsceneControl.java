package K.Othermod.NH;

import arc.Events;
import arc.func.Func;
import arc.scene.Action;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static K.Othermod.NH.NHGroups.clear;
/**
 * Controls the execution of cutscene action buses.
 * Manages main bus queue, sub buses, and waiting periods between cutscenes.
 */
public class CutsceneControl {

    public static final String CSS_ACTION = "[CSS_ACTION]";
    public static ObjectMap<String, Func<String[], ? extends Action>> actionParser = new ObjectMap<>();

    // Whether currently waiting between cutscenes
    public boolean waiting = false;
    // Time spacing between cutscenes in ticks
    public float waitSpacing = 60f;
    // Current wait timer
    public float waitTimer = 0f;

    public CutsceneControl() {
        Events.on(EventType.WorldLoadEvent.class, event -> clear());
    }


    public static Seq<String> parseLine(String code) {
        String[] lines = code.split("\\R");
        return Seq.with(lines);
    }

    public static Seq<String> parseToken(String line) {
        Seq<String> result = new Seq<>();
        Matcher matcher = Pattern.compile("<([^>]*)>|\\S+").matcher(line);
        while (matcher.find()) {
            result.add(matcher.group(1) != null ? matcher.group(1) : matcher.group());
        }
        return result;
    }

    public static Action parseSpecialEvent(String headerLine, Seq<String> unitLines) {
        try {
            Log.info("Parsing String: " + headerLine);
            Seq<String> tokens = parseToken(headerLine);
            tokens.remove(0);

            // new header is at most team/alert/range/override/[x/y] (4 or 6 tokens);
            // larger payloads are legacy single-line event-special with inline units
            if (tokens.size <= 6) {
                tokens.add(String.valueOf(unitLines.size));
                for (String unitLine : unitLines) {
                    Seq<String> unitTokens = parseToken(unitLine);
                    if (unitTokens.isEmpty()) continue;
                    unitTokens.remove(0);
                    tokens.addAll(unitTokens);
                }
            }

            Func<String[], ? extends Action> parser = actionParser.get("event-special");
            return parser.get(tokens.toArray(String.class));
        } catch (Exception e) {
            Log.err("Error when parsing special event: " + headerLine);
            Log.err(e);
        }
        return null;
    }

    public static Action parseAction(String tokens) {
        try {
            Log.info("Parsing String: " + tokens);
            Seq<String> tokensArray = parseToken(tokens);
            String actionName = tokensArray.remove(0);
            String[] args = tokensArray.toArray(String.class);
            return actionParser.get(actionName).get(args);
        } catch (Exception e) {
            Log.err("Error when parsing token: " + tokens);
            Log.err(e);
        }
        return null;
    }

    public static void saveActionBus(String name, String action) {
        Vars.state.rules.tags.put(CSS_ACTION + name, action);
    }


    public void update() {
        updateMainBus();
        updateWaiting();
    }

    private void updateMainBus() {
    }

    private void updateWaiting() {
        if (!waiting) return;

        waitTimer += Time.delta;
        if (waitTimer >= waitSpacing) {
            waitTimer = 0f;
            waiting = false;
        }
    }
}
