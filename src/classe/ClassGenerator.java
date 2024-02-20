package classe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Paths;
import java.nio.file.Path;
import database_details.DatabaseDetails;

public class ClassGenerator {
    private static final String TEMPLATE_FILE = "./ClassTemplate.txt";
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
    private String protect;
    private String annotation;
    private String impor;
    private String pack;
    private String packName;
    private String thi;
    private String attribution;
    private String retour;

    private static final Map<String, String> CONFIG_MAP = new HashMap<>();

    static {
        CONFIG_MAP.put("SPRING", "config_file/javaconfig.xml");
        CONFIG_MAP.put("C#", "config_file/csconfig.xml");
    }

    public ClassGenerator(String techno) throws Exception {
        this.CONFIG_FILE = this.getConfigFil(techno);
        this.setParameter();
    }

    public String getConfigFil(String techno) {
        String csType = CONFIG_MAP.get(techno.toUpperCase());
        return (csType != null) ? csType : "";
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
        this.protect = this.getValue("util", "protected");
        this.impor = this.getValue("util", "import");
        this.pack = this.getValue("util", "package");
        this.packName = this.getValue("util", "packageName");
        this.thi = this.getValue("util", "this");
        this.attribution = this.getValue("util", "attribution");
        this.retour = this.getValue("util", "return");
        this.annotation = this.getValue("util", "annotation");
    }

    public HashMap<String, ArrayList<Attribut>> data() throws Exception {
        ArrayList<DatabaseDetails> details = DatabaseDetails.getDatabaseDetailsFromDatabase();
        HashMap<String, ArrayList<Attribut>> hs = new HashMap<String, ArrayList<Attribut>>();
        for (int i = 0; i < details.size(); i++) {
            DatabaseDetails databaseDetails = details.get(i);
            if (hs.containsKey(databaseDetails.getTableName())) {
                ArrayList<Attribut> arr = hs.get(databaseDetails.getTableName());
                arr.add(new Attribut(databaseDetails.getColumnName(),
                        this.getValue("databaseRef", databaseDetails.getColumnType()),
                        this.getValue("import", databaseDetails.getColumnType())));
                hs.put(databaseDetails.getTableName(), arr);
            } else {
                ArrayList<Attribut> arr = new ArrayList<Attribut>();
                arr.add(new Attribut(databaseDetails.getColumnName(),
                        this.getValue("databaseRef", databaseDetails.getColumnType()),
                        this.getValue("import", databaseDetails.getColumnType())));
                hs.put(databaseDetails.getTableName(), arr);
            }
        }
        return hs;
    }

    public String importe(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String importation = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            if (!importation.contains(attribut.get(i).getImportation())) {
                importation += impor + " " + attribut.get(i).getImportation() + endLine + "\n";
            }
        }
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

    public String attribut(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String attributes = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        attributes += "\t@Id\n\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
        for (int i = 0; i < attribut.size(); i++) {
            attributes += "\t" + prive + " " + attribut.get(i).getType() + " " + attribut.get(i).getNom() + endLine
                    + "\n";
        }
        return attributes;
    }

    public void generate() {
        try {
            HashMap<String, ArrayList<Attribut>> hs = this.data();
            Set<Entry<String, ArrayList<Attribut>>> entrees = hs.entrySet();
            for (Entry<String, ArrayList<Attribut>> table : entrees) {
                String tableName = table.getKey();
                this.generate(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generate(String tableName) {
        try {
            HashMap<String, ArrayList<Attribut>> hs = this.data();
            String attributs = this.attribut(hs, tableName);
            String imports = this.importe(hs, tableName);
            String className = capitalizeFirstLetter(tableName);
            String template = loadTemplateFromFile();
            String packa = this.pack();
            String setters = this.setter(hs, tableName);
            String getters = this.getter(hs, tableName);
            String constructor = this.constructeur(hs, tableName);
            String emptyconstructor = this.constructeurvide(hs, tableName);
            template = template.replace("##PACKAGE##", packa);
            template = template.replace("##STARTPACKAGE##", startPackage);
            template = template.replace("##DEFINITION##", definition);
            template = template.replace("#CLASS_NAME#", className);
            template = template.replace("##STARTCLASS##", startClass);
            template = template.replace("#ATTRIBUTS#", attributs);
            template = template.replace("##IMPORTS##", imports);
            template = template.replace("#SETTERS#", setters);
            template = template.replace("#GETTERS#", getters);
            template = template.replace("#CONSTRUCTOR#", constructor);
            template = template.replace("#EMPTYCONSTRUCTOR#", emptyconstructor);
            template = template.replace("##ENDCLASS##", endClass);
            template = template.replace("##ENDPACKAGE##", endPackage);
            template = template.replace("##ANNOTATION##", annotation);

            File folder = new File(packName);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    try (BufferedWriter writer = new BufferedWriter(
                            new FileWriter(packName + "/" + className + extension))) {
                        writer.write(template);
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new Exception("Error while creating folder");
                }
            } else {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(packName + "/" + className + extension))) {
                    writer.write(template);
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String loadTemplateFromFile() throws IOException {
        StringBuilder template = new StringBuilder();
        Path projectRoot = Paths.get("../..").toAbsolutePath();
        System.setProperty("user.dir", projectRoot.toString());
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMPLATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line).append("\n");
            }
        }
        return template.toString();
    }

    public String setter(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String setter = "";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            setter += "\t" + publie + " void set" + capitalizeFirstLetter(attribut.get(i).getNom()) + "( "
                    + attribut.get(i).getType() + " " + attribut.get(i).getNom() + " )\n\t" + startBlock + "\n\t\t"
                    + thi
                    + attribut.get(i).getNom() + attribution
                    + attribut.get(i).getNom() + endLine + " \n\t" + endBlock + "\n\n";
        }
        return setter;
    }

    public String getter(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String getter = "";
        ArrayList<Attribut> attribut = hs.get(tableName);

        for (int i = 0; i < attribut.size(); i++) {
            getter += "\t" + publie + " " + attribut.get(i).getType() + " get"
                    + capitalizeFirstLetter(attribut.get(i).getNom()) + "()\n\t" + startBlock + "\n\t\t"
                    + retour + " "
                    + thi
                    + attribut.get(i).getNom() + endLine + " \n\t" + endBlock + "\n\n";
        }
        return getter;
    }

    public String constructeur(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String constructor = "\t" + publie + " " + capitalizeFirstLetter(tableName) + "(";
        ArrayList<Attribut> attribut = hs.get(tableName);
        for (int i = 0; i < attribut.size(); i++) {
            constructor += attribut.get(i).getType() + " " + attribut.get(i).getNom();
            if (i != attribut.size() - 1) {
                constructor += ", ";
            }
        }
        constructor += " )\n\t" + startBlock;
        for (int i = 0; i < attribut.size(); i++) {
            constructor += "\n\t\t";
            constructor += thi + "set" + capitalizeFirstLetter(attribut.get(i).getNom()) + "("
                    + attribut.get(i).getNom()
                    + ")" + endLine + " ";
        }
        constructor += "\n\t" + endBlock;
        return constructor;
    }

    public String constructeurvide(HashMap<String, ArrayList<Attribut>> hs, String tableName) throws Exception {
        String constructor = "\t" + publie + " " + capitalizeFirstLetter(tableName) + "()\n\t" + startBlock + "\n\n\t"
                + endBlock;
        return constructor;
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public String pack() throws Exception {
        String packa = "";
        packa += pack + " " + packName;
        return packa;
    }

    public String getValue(String parent, String element) throws Exception {
        String type = "";
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
}
