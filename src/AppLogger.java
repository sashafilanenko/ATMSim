import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class AppLogger {
    private static final String LOG_DIR = "logs";               // папка для логов (можно изменить)
    private static final String LOG_PATTERN = "mybank.log.%u.%g"; // шаблон имени файла
    private static final int LIMIT = 1024 * 1024;               // 1 MB на файл
    private static final int COUNT = 5;                         // хранить до 5 файлов (ротация)
    private static final Level DEFAULT_LEVEL = Level.INFO;

    private static Logger rootLogger;

    private AppLogger(){}

    public static synchronized void init(){
        if(rootLogger != null) return;

        try {
            Path dir = Path.of(LOG_DIR);
            if(!Files.exists(dir)){
                Files.createDirectories(dir);
            }

            rootLogger = Logger.getLogger("");

            for(Handler h : rootLogger.getHandlers()){
                rootLogger.removeHandler(h);
            }

            FileHandler fh = new FileHandler(LOG_DIR + "/" + LOG_PATTERN, LIMIT, COUNT, true);
            fh.setLevel(DEFAULT_LEVEL);
            fh.setFormatter(new SimpleFormatter(){
                private final DateTimeFormatter dt = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                @Override
                public synchronized String format(LogRecord record) {
                    String ts = ZonedDateTime.now(ZoneId.systemDefault()).format(dt);
                    String level = record.getLevel().getName();
                    String loggerName = record.getLoggerName();
                    String msg = formatMessage(record);
                    String throwable = "";
                    if (record.getThrown() != null) {
                        StringBuilder sb = new StringBuilder();
                        Throwable t = record.getThrown();
                        sb.append("\n").append(t.toString()).append("\n");
                        for (StackTraceElement el : t.getStackTrace()) {
                            sb.append("\tat ").append(el.toString()).append("\n");
                        }
                        throwable = sb.toString();
                    }
                    return String.format("%s | %s | %s | %s%s%n", ts, level, loggerName, msg, throwable);
                }
            });

            rootLogger.addHandler(fh);
            rootLogger.setLevel(DEFAULT_LEVEL);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Logger getLogger(String name) {
        if (rootLogger == null) init();
        return Logger.getLogger(name);
    }
}
