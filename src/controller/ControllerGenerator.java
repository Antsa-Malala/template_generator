package controller;

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

public class ControllerGenerator {
    private static final String TEMPLATE_FILE = "./ControllerTemplate.txt";
    private final String CONFIG_FILE;
    private String definition;
    private String endLine;
    private String endBlock;
    private String startBlock;
    private String endClass;
    private String startClass;
    private String endPackage;
    private String startPackage;
    private String extension;
    private String prive;
    private String publie;
    private String impor;
    private String pack;
    private String packName;
    private String modelPackage;
    private String thi;
    private String attribution;
    private String retour;
    private String contAnnot;
    private String constAnnot;
    private String root;
    private String extend;
    private String contMother;
    private String repo;
    private String cors;

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("C#", "config_file/csControllerconfig.xml");
        CONFIG_MAP.put("SPRING", "config_file/springconfig.xml");
    }

    public ControllerGenerator(String techno) throws Exception {
        this.CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();

    }

    public void setParameter() throws Exception {
        this.definition = this.getValue("util", "definition");
        this.endLine = this.getValue("util", "endLine");
        this.endBlock = this.getValue("util", "endBlock");
        this.startBlock = this.getValue("util", "startBlock");
        this.endClass = this.getValue("util", "endClass");
        this.startClass = this.getValue("util", "startClass");
        this.endPackage = this.getValue("util", "endPackage");
        this.startPackage = this.getValue("util", "startPackage");
        this.extension = this.getValue("util", "extension");
        this.prive = this.getValue("util", "private");
        this.publie = this.getValue("util", "public");
        this.impor = this.getValue("util", "import");
        this.pack = this.getValue("util", "package");
        this.packName = this.getValue("util", "packageName");
        this.modelPackage = this.getValue("util", "modelPackage");
        this.thi = this.getValue("util", "this");
        this.attribution = this.getValue("util", "attribution");
        this.retour = this.getValue("util", "return");
        this.contAnnot = this.getValue("util", "controllerAnnotation");
        this.constAnnot = this.getValue("util", "constructorAnnotation");
        this.root = this.getValue("util", "root");
        this.extend = this.getValue("util", "extends");
        this.contMother = this.getValue("util", "controllerMotherClass");
        this.repo = this.getValue("util", "repository");
        this.cors = this.getValue("util", "cors");
    }

    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
    }

    public String importe(String className) throws Exception {
        String importation = "";
        String[] util = this.necessaryImport();
        for (int i = 0; i < util.length; i++) {
            importation += impor + " " + util[i] + endLine + "\n";
        }
        return importation;
    }

    public String[] necessaryImport() throws Exception {
        String[] nec = new String[0];
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = (Document) builder.parse(CONFIG_FILE);

            NodeList databaseRefList = ((org.w3c.dom.Document) document).getElementsByTagName("necessaryImport");
            if (databaseRefList != null) {
                Element databaseRef = (Element) databaseRefList.item(0);
                NodeList elements = databaseRef.getElementsByTagName("utilImport");
                if (elements != null) {
                    nec = new String[elements.getLength()];
                    for (int i = 0; i < elements.getLength(); i++) {
                        Node elmt = elements.item(i);
                        if (elmt != null) {
                            nec[i] = elmt.getTextContent();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nec;
    }

    public String pack() throws Exception {
        String packa = "";
        packa += pack + " " + packName;
        return packa;

    }

    public void generate(String className) throws Exception {
        try {
            className = ControllerGenerator.capitalizeFirstLetter(className);
            String template = loadTemplateFromFile();

            String pack = this.pack();
            template = template.replace("##PACKAGE##", pack);

            String imports = this.importe(className);
            template = template.replace("##IMPORTS##", imports);

            template = template.replace("##CONTROLLER_ANNOTATION##", contAnnot);

            template = template.replace("##ROOT##", this.root);

            template = template.replace("##DEFINITION##", definition);

            String kilasy = this.getName(className);
            template = template.replace("#CLASSNAME#", kilasy);

            String exten = this.extend();
            template = template.replace("##EXTENDS##", exten);
            template = template.replace("##CORS##", cors);

            String attribut = this.attributes(className);
            template = template.replace("#ATTRIBUTES#", attribut);
            template = template.replace("#CONSTRUCTOR_ANNOTATION#", constAnnot);

            template = this.root(template);
            template = this.returns(template, className);

            template = this.args(template);

            template = this.contents(template);

            template = template.replace("#CLASS_NAME#", className);
            template = template.replace("#CLASS_NAME_2#", className.toLowerCase());
            template = template.replace("##STARTCLASS##", startClass);
            template = template.replace("##ENDCLASS##", endClass);
            template = template.replace("##STARTBLOCK##", startBlock);
            template = template.replace("##ENDBLOCK##", endBlock);
            template = template.replace("##STARTPACKAGE##", startPackage);
            template = template.replace("#MODEL_PACKAGE#", modelPackage);
            template = template.replace("##PRIVATE##", prive);
            template = template.replace("##PUBLIC##", publie);
            template = template.replace("##ENDPACKAGE##", endPackage);

            File folder = new File(packName);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(packName + "/" + kilasy + extension))) {
                        writer.write(template);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception("Error while creating folder");
                }
            } else {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(packName + "/" + kilasy + extension))) {
                    writer.write(template);
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
        Path filePath = Paths.get("./" + modelPackage, filename);
        if (Files.exists(filePath)) {
            result = true;
        }
        return result;
    }

    private String getName(String className) throws Exception {
        String kilasy = capitalizeFirstLetter(className);
        if (!this.checkClasse(kilasy)) {
            throw new Exception("Error finding source class while generating controller");
        } else {
            kilasy = kilasy + "Controller";
        }
        return kilasy;
    }

    private String extend() {
        return extend + " " + capitalizeFirstLetter(contMother);
    }

    private String attributes(String className) {
        String attribut = " " + repo + endLine;
        return attribut;
    }

    private String annotationfindAll() throws Exception {
        String annotation = this.getValue("root", "findAll");
        return annotation;
    }

    private String annotationfindOne() throws Exception {
        String annotation = this.getValue("root", "findOne");
        return annotation;
    }

    private String annotationInsert() throws Exception {
        String annotation = this.getValue("root", "insert");
        return annotation;
    }

    private String annotationUpdate() throws Exception {
        String annotation = this.getValue("root", "update");
        return annotation;
    }

    private String annotationDelete() throws Exception {
        String annotation = this.getValue("root", "delete");
        return annotation;
    }

    private String argsfindAll() throws Exception {
        String args = this.getValue("args", "findAll");
        return args;
    }

    private String argsfindOne() throws Exception {
        String args = this.getValue("args", "findOne");
        return args;
    }

    private String argsInsert() throws Exception {
        String args = this.getValue("args", "insert");
        return args;
    }

    private String argsUpdate() throws Exception {
        String args = this.getValue("args", "update");
        return args;
    }

    private String argsDelete() throws Exception {
        String args = this.getValue("args", "delete");
        return args;
    }

    private String argsConstructor() throws Exception {
        String args = this.getValue("args", "constructor");
        return args;
    }

    public String root(String template) throws Exception {
        String annotationfindAll = this.annotationfindAll();
        template = template.replace("#ROOT_FIND_ALL#", annotationfindAll);
        String annotationInsertForm = this.annotationfindOne();
        template = template.replace("#ROOT_FIND_ONE#", annotationInsertForm);
        String annotationInsert = this.annotationInsert();
        template = template.replace("#ROOT_INSERT#", annotationInsert);
        String annotationDelete = this.annotationDelete();
        template = template.replace("#ROOT_DELETE#", annotationDelete);
        String annotationUpdate = this.annotationUpdate();
        template = template.replace("#ROOT_UPDATE#", annotationUpdate);
        return template;
    }

    public String args(String template) throws Exception {
        String argsfindAll = this.argsfindAll();
        template = template.replace("#ARGS_FIND_ALL#", argsfindAll);
        String argsinsertForm = this.argsfindOne();
        template = template.replace("#ARGS_FIND_ONE#", argsinsertForm);
        String argsInsert = this.argsInsert();
        template = template.replace("#ARGS_INSERT#", argsInsert);
        String argsDelete = this.argsDelete();
        template = template.replace("#ARGS_DELETE#", argsDelete);
        String argsUpdate = this.argsUpdate();
        template = template.replace("#ARGS_UPDATE#", argsUpdate);
        String argsConstruct = this.argsConstructor();
        template = template.replace("#CONSTRUCTOR_ARGS#", argsConstruct);
        return template;
    }

    public String returns(String template, String className) throws Exception {
        template = template.replace("#RETURN_FIND_ALL#",
                this.getValue("returnType", "findAll"));
        template = template.replace("#RETURN_FIND_ONE#",
                this.getValue("returnType", "findOne"));
        template = template.replace("#RETURN_INSERT#",
                this.getValue("returnType", "insert"));
        template = template.replace("#RETURN_UPDATE#",
                this.getValue("returnType", "update"));

        template = template.replace("#RETURN_DELETE#",
                this.getValue("returnType", "delete"));
        return template;
    }

    public String contents(String template) throws Exception {
        String contentfindAll = this.contentfindAll();
        template = template.replace("#CONTENT_FIND_ALL#", contentfindAll);
        String contentInsert = this.contentInsert();
        template = template.replace("#CONTENT_INSERT#", contentInsert);
        String contentInsertForm = this.contentfindOne();
        template = template.replace("#CONTENT_FIND_ONE#", contentInsertForm);
        String contentDelete = this.contentDelete();
        template = template.replace("#CONTENT_DELETE#", contentDelete);
        String contentUpdate = this.contentUpdate();
        template = template.replace("#CONTENT_UPDATE#", contentUpdate);
        String contentConstruct = this.contentConstructor();
        template = template.replace("#CONSTRUCTOR_CONTENT#", contentConstruct);
        String suppContent = this.suppContent();
        template = template.replace("#SUPP_CONTENT#", suppContent);
        return template;
    }

    private String contentfindAll() throws Exception {
        String content = this.getOneValue("findAllContent");
        return content;
    }

    private String contentInsert() throws Exception {
        String content = this.getOneValue("insertContent");
        return content;
    }

    private String contentfindOne() throws Exception {
        String content = this.getOneValue("findOneContent");
        return content;
    }

    private String contentUpdate() throws Exception {
        String content = this.getOneValue("updateContent");
        return content;
    }

    private String contentDelete() throws Exception {
        String content = this.getOneValue("deleteContent");
        return content;
    }

    private String contentConstructor() throws Exception {
        String content = this.getOneValue("constructorContent");
        return content;
    }

    private String suppContent() throws Exception {
        String content = this.getOneValue("suppContent");
        return content;
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
