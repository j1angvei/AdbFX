package cn.j1angvei.adbfx.adb.pm;

import com.android.ddmlib.MultiLineReceiver;
import javafx.beans.property.ListProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * task to get package list
 */
public class PackageListTask extends PackageManager<List<String>> {
    private static final String PREFIX = "package:";

    private final ListProperty<String> arguments;

    public PackageListTask(ListProperty<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    protected List<String> call() throws Exception {

        List<String> packageInfoList = new ArrayList<>();

        StringBuilder cmdBuilder = new StringBuilder("pm list packages");
        List<String> args = arguments.get();
        args.forEach(s -> cmdBuilder.append(" ").append(s));

        getChosenDevice().executeShellCommand(cmdBuilder.toString(), new MultiLineReceiver() {
            @Override
            public void processNewLines(String[] lines) {
                Stream.of(lines).filter(s -> s != null && s.startsWith(PREFIX)).forEach(e -> packageInfoList.add(e.substring(PREFIX.length())));
            }

            @Override
            public boolean isCancelled() {
                return false;
            }
        });
        return packageInfoList;
    }
}
