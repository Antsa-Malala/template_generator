package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FormGenerator {
    private static final String TEMPLATE_FILE = "./ReactFormTemplate.txt";
    private final String CONFIG_FILE;
    private String url;
    private String insertUrl;
    private String methode;
    private String insertMethode;
    private String listLink;
    private String cssContent;
    private String dossier;
    private String extension;

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("SPRING", "config_file/springform.xml");
    }

    public FormGenerator(String techno) throws Exception {
        this.CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();

    }

    public void setParameter() throws Exception {
        this.url = this.getValue("util", "url");
        this.insertUrl = this.getValue("util", "insertUrl");
        this.methode = this.getValue("methods", "List");
        this.insertMethode = this.getValue("methods", "Insert");
        this.extension = this.getValue("util", "extension");
        this.dossier = this.getValue("util", "dossier");
        this.listLink = this.getValue("util", "listLink");
        this.cssContent = this.getOneValue("cssContent");

    }

    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
    }

    public void generate(String className) throws Exception {
        try {
            className = FormGenerator.capitalizeFirstLetter(className);
            String template = loadTemplateFromFile();

            template = template.replace("#URL#", url);
            template = template.replace("#INSERT_URL#", insertUrl);
            template = template.replace("#INSERT_METHOD#", insertMethode);
            template = template.replace("#LIST_LINK#", listLink);
            template = template.replace("#METHOD_LIST#", methode);
            template = template.replace("#CLASS_NAME_2#", className.toLowerCase());

            File folder = new File(dossier);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(dossier + "/Form" + extension))) {
                        writer.write(template);
                    } catch (Exception e) {
                        throw e;
                    }
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(dossier + "/Form.css"))) {
                        writer.write(cssContent);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception("Error while creating folder");
                }
            } else {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(dossier + "/Form" + extension))) {
                    writer.write(template);
                } catch (Exception e) {
                    throw e;
                }
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(dossier + "/Form.css"))) {
                    writer.write(cssContent);
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String loadTemplateFromFile() throws Exception {
        StringBuilder template = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMPLATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line).append("\n");
            }
        }
        return template.toString();
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private boolean checkClasse(String className) {
        boolean result = false;
        String filename = className + extension;
        Path filePath = Paths.get("./dossier", filename);
        if (Files.exists(filePath)) {
            result = true;
        }
        return result;
    }

    public String getValue(String parent, String element) throws Exception {
        String type = "";
        Path projectRoot = Paths.get(".").toAbsolutePath();
        System.setProperty("user.dir", projectRoot.toString());

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            NodeList databaseRefList = ((org.w3c.dom.Document) document).getElementsByTagName(parent);
            if (databaseRefList != null) {
                Element databaseRef = (Element) databaseRefList.item(0);
                if (databaseRef != null) {
                    Node elmt = databaseRef.getElementsByTagName(element).item(0);
                    if (elmt != null) {
                        type = elmt.getTextContent();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    public String getOneValue(String parent) throws Exception {
        String type = "";
        Path projectRoot = Paths.get(".").toAbsolutePath();
        System.setProperty("user.dir", projectRoot.toString());

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            Element element = (Element) document.getElementsByTagName(parent).item(0);
            if (element != null) {
                type = element.getTextContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

}
