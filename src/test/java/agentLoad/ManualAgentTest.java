package agentLoad;

import com.sun.tools.attach.VirtualMachine;
import jline.console.ConsoleReader;
import utils.ProcessUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class ManualAgentTest {

    public static void main(String[] args) throws Exception {
        Map<Long, String> longStringMap = ProcessUtils.listProcessByJps(false);
        longStringMap.forEach((k, v) -> {
            System.out.println(v);
        });

        final ConsoleReader consoleReader = new ConsoleReader(System.in, System.out);
        consoleReader.setHandleUserInterrupt(true);

        consoleReader.getOutput().write("选择pid: ");
        consoleReader.getOutput().flush();

        // 获取pid
        String pid = readString(consoleReader);

        VirtualMachine attach = VirtualMachine.attach(pid);
        attach.loadAgent("/Users/liuyong/Downloads/work/agent-template/target/agent-template-jar-with-dependencies.jar");

        try {
            String command;
            while (true) {
                command = readString(consoleReader);
                System.out.println(command);
            }
        } finally {
            attach.detach();
        }
    }

    public static String readString(ConsoleReader consoleReader) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(consoleReader.getInput()));
        return br.readLine();
    }
}
