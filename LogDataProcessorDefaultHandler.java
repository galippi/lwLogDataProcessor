package lwLogDataProcessor;

public class LogDataProcessorDefaultHandler implements LogDataProcessorInterface {

    @Override
    public boolean isHandler() {
        return true;
    }

    @Override
    public void process(String data) {
        System.out.println("LogDataProcessorDefaultHandler.process data=" + data);
    }
}