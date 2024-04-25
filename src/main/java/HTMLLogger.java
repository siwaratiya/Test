import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HTMLLogger {
    private static final String LOG_FILE_PATH = "src/main/resources/templates/log.html";

    public static void log(String message, String data) {
        try {
            // Charger le fichier de log existant ou créer un nouveau document HTML vide
            Document doc;
            File logFile = new File(LOG_FILE_PATH);
            if (logFile.exists()) {
                doc = Jsoup.parse(logFile, "UTF-8");
            } else {
                doc = new Document("");
            }

            // Ajouter un nouvel enregistrement de log
            Element logEntry = new Element("div");
            logEntry.append("<h1>" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + message + " - " + data + "</h1>");
            // Ajouter l'enregistrement de log au document
            doc.body().appendChild(logEntry);

            // Enregistrer le document HTML mis à jour dans le fichier de log
            FileWriter writer = new FileWriter(LOG_FILE_PATH);
            writer.write(doc.outerHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
