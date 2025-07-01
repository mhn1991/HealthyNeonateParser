import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.Statement;
import org.apache.poi.POITextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;


/**
 *
 * @author Mojtaba
 */
public class Parser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        String[][] NeofaxTable = new String[196][16];

        NeofaxTable[0][0] = "Name:";
        NeofaxTable[0][1] = "Dose:";
        NeofaxTable[0][2] = "Administration:";
        NeofaxTable[0][3] = "Uses:";
        NeofaxTable[0][4] = "Contraindications/Precautions:";
        NeofaxTable[0][5] = "Black Box Warning:";
        NeofaxTable[0][6] = "Pharmacology:";
        NeofaxTable[0][7] = "Adverse Effects:";
        NeofaxTable[0][8] = "Monitoring:";
        NeofaxTable[0][9] = "Special Considerations/Preparation:";
        NeofaxTable[0][10] = "Solution Compatibility:";
        NeofaxTable[0][11] = "Solution Incompatibility:";
        NeofaxTable[0][12] = "Terminal Injection Site Compatibility:";
        NeofaxTable[0][13] = "Terminal Injection Site Incompatibility:";
        NeofaxTable[0][14] = "Table:";
        NeofaxTable[0][15] = "References:";



        String st = "";

        try {
            PDDocument document = null;
            document = PDDocument.load(new File("C:\\Users\\mhn19\\Downloads\\neofax(1).pdf"));
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper Tstripper = new PDFTextStripper();
                st = Tstripper.getText(document);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }





        st = st.replaceAll("Micormedex NeoFax Essentials 2014 \r\n \\d{1,3} \r\n", "");
        st = st.replaceAll("\r\nDose", "\r\n\\$\\$\\$ Dose");
        st = st.replaceAll("\r\nTitle", "\r\n\\$\\$\\$ Title");
        st = st.replaceAll("\r\nReferences", "\r\n\\$\\$\\$ References");
        st = st.replaceAll("\r\nAdministration", "\r\n\\$\\$\\$ Administration");
        st = st.replaceAll("\r\nUses", "\r\n\\$\\$\\$ Uses");
        st = st.replaceAll("\r\nContraindications/Precautions", "\r\n\\$\\$\\$ Contraindications/Precautions");
        st = st.replaceAll("\r\nBlack Box Warning", "\r\n\\$\\$\\$ Black Box Warning");
        st = st.replaceAll("\r\nPharmacology", "\r\n\\$\\$\\$ Pharmacology");
        st = st.replaceAll("\r\nAdverse Effects  \r\n", "\r\n\\$\\$\\$ Adverse Effects  \r\n");
        st = st.replaceAll("\r\nMonitoring", "\r\n\\$\\$\\$ Monitoring");
        st = st.replaceAll("\r\nSpecial Considerations/Preparation", "\r\n\\$\\$\\$ Special Considerations/Preparation");
        st = st.replaceAll("\r\nSolution Compatibility", "\r\n\\$\\$\\$ Solution Compatibility");
        st = st.replaceAll("\r\nSolution Incompatibility", "\r\n\\$\\$\\$ Solution Incompatibility");
        st = st.replaceAll("\r\nTerminal Injection Site Compatibility", "\r\n\\$\\$\\$ Terminal Injection Site Compatibility");
        st = st.replaceAll("\r\nTerminal Injection Site Incompatibility", "\r\n\\$\\$\\$ Terminal Injection Site Incompatibility");
        st = st.replaceAll("\r\nTable", "\r\n\\$\\$\\$ Table");
        st = st.replaceAll("\r\n(\\d)\r\n", "$1");


        int indexFirstTitle = 0;
        int indexsecondTitle = 0;
        int number = 1;

//
//        System.out.print(st);
//        System.exit(0);


        while(true){

            indexFirstTitle = st.indexOf("$$$ Title", indexsecondTitle + 9);
            indexsecondTitle = st.indexOf("$$$ Title", indexFirstTitle + 9);

            if(indexFirstTitle == -1){
                break;
            }

            //Names:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int endLineChar;
            int firstOfName;
            int endOfName;

            endLineChar = indexFirstTitle - 11;
            while (st.charAt(endLineChar)!= '\n') {
                endLineChar--;
            }
            firstOfName = endLineChar + 1;
            endOfName = st.indexOf("\r\n", firstOfName)-1;

            NeofaxTable[number][0] = st.substring(firstOfName, endOfName);
            for (int i=0 ; i < 10 ; i++) {   // bardashtane shomare ghable name
                if(NeofaxTable[number][0].charAt(i) == ' '){
                    NeofaxTable[number][0]=NeofaxTable[number][0].substring(i+1);
                    break;
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


//            //Does:
//            /////////////////////////////////////////////////////////////////////////////////////////////
//            int firstOfDose;
//            int endOfDose;
//            firstOfDose = st.indexOf("$$$ Dose", indexFirstTitle);
//            if (firstOfDose!=-1 && firstOfDose<indexsecondTitle){
//                endOfDose = st.indexOf($$$", firstOfDose)-3;
//                NeofaxTable[number][1] = st.substring(firstOfDose, endOfDose);
//            }
//            /////////////////////////////////////////////////////////////////////////////////////////////

            //Administration:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfAdministration;
            int endOfAdministration;
            firstOfAdministration = st.indexOf("$$$ Administration", indexFirstTitle);
            if (firstOfAdministration!=-1 && firstOfAdministration<indexsecondTitle){
                endOfAdministration = st.indexOf("$$$", firstOfAdministration+4)-3;
                NeofaxTable[number][2] = st.substring(firstOfAdministration+4, endOfAdministration);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Uses:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfUses;
            int endOfUses;
            firstOfUses = st.indexOf("$$$ Uses", indexFirstTitle);
            if (firstOfUses!=-1 && firstOfUses<indexsecondTitle){
                endOfUses = st.indexOf("$$$ ", firstOfUses+4)-3;
                NeofaxTable[number][3] = st.substring(firstOfUses+4, endOfUses);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Contraindications/Precautions:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfContraindicationsPrecautions;
            int endOfContraindicationsPrecautions;
            firstOfContraindicationsPrecautions = st.indexOf("$$$ Contraindications/Precautions", indexFirstTitle);
            if (firstOfContraindicationsPrecautions!=-1 && firstOfContraindicationsPrecautions<indexsecondTitle){
                endOfContraindicationsPrecautions = st.indexOf("$$$", firstOfContraindicationsPrecautions+4)-3;
                NeofaxTable[number][4] = st.substring(firstOfContraindicationsPrecautions+4, endOfContraindicationsPrecautions);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Black Box Warning:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfBlackBoxWarning;
            int endOfBlackBoxWarning;
            firstOfBlackBoxWarning = st.indexOf("$$$ Black Box Warning", indexFirstTitle);
            if (firstOfBlackBoxWarning!=-1 && firstOfBlackBoxWarning<indexsecondTitle){
                endOfBlackBoxWarning = st.indexOf("$$$", firstOfBlackBoxWarning+4)-3;
                NeofaxTable[number][5] = st.substring(firstOfBlackBoxWarning+4, endOfBlackBoxWarning);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Pharmacology:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfPharmacology;
            int endOfPharmacology;
            firstOfPharmacology = st.indexOf("$$$ Pharmacology", indexFirstTitle);
            if (firstOfPharmacology!=-1 && firstOfPharmacology<indexsecondTitle){
                endOfPharmacology = st.indexOf("$$$", firstOfPharmacology+4)-3;
                NeofaxTable[number][6] = st.substring(firstOfPharmacology+4, endOfPharmacology);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Adverse Effects:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfAdverseEffects;
            int endOfAdverseEffects;
            firstOfAdverseEffects = st.indexOf("$$$ Adverse Effects  \r\n", indexFirstTitle);
            if (firstOfAdverseEffects!=-1 && firstOfAdverseEffects<indexsecondTitle){
                endOfAdverseEffects = st.indexOf("$$$", firstOfAdverseEffects+4)-3;
                NeofaxTable[number][7] = st.substring(firstOfAdverseEffects+4, endOfAdverseEffects);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Monitoring:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfMonitoring;
            int endOfMonitoring;
            firstOfMonitoring = st.indexOf("$$$ Monitoring", indexFirstTitle);
            if (firstOfMonitoring!=-1 && firstOfMonitoring<indexsecondTitle){
                endOfMonitoring = st.indexOf("$$$", firstOfMonitoring+4)-3;
                NeofaxTable[number][8] = st.substring(firstOfMonitoring+4, endOfMonitoring);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Special Considerations/Preparation:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfSpecialConsiderationsPreparation;
            int endOfSpecialConsiderationsPreparation;
            firstOfSpecialConsiderationsPreparation = st.indexOf("$$$ Special Considerations/Preparation", indexFirstTitle);
            if (firstOfSpecialConsiderationsPreparation!=-1 && firstOfSpecialConsiderationsPreparation<indexsecondTitle){
                endOfSpecialConsiderationsPreparation = st.indexOf("$$$", firstOfSpecialConsiderationsPreparation+4)-3;
                NeofaxTable[number][9] = st.substring(firstOfSpecialConsiderationsPreparation+4, endOfSpecialConsiderationsPreparation);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Solution Compatibility:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfSolutionCompatibility;
            int endOfSolutionCompatibility;
            firstOfSolutionCompatibility = st.indexOf("$$$ Solution Compatibility", indexFirstTitle);
            if (firstOfSolutionCompatibility!=-1 && firstOfSolutionCompatibility<indexsecondTitle){
                endOfSolutionCompatibility = st.indexOf("$$$", firstOfSolutionCompatibility+4)-3;
                NeofaxTable[number][10] = st.substring(firstOfSolutionCompatibility+4, endOfSolutionCompatibility);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            //Solution Incompatibility:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfSolutionIncompatibility;
            int endOfSolutionIncompatibility;
            firstOfSolutionIncompatibility = st.indexOf("$$$ Solution Incompatibility", indexFirstTitle);
            if (firstOfSolutionIncompatibility!=-1 && firstOfSolutionIncompatibility<indexsecondTitle){
                endOfSolutionIncompatibility = st.indexOf("$$$", firstOfSolutionIncompatibility+4)-3;
                NeofaxTable[number][11] = st.substring(firstOfSolutionIncompatibility+4, endOfSolutionIncompatibility);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Terminal Injection Site Compatibility:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfTerminalInjectionSiteCompatibility;
            int endOfTerminalInjectionSiteCompatibility;
            firstOfTerminalInjectionSiteCompatibility = st.indexOf("$$$ Terminal Injection Site Compatibility", indexFirstTitle);
            if (firstOfTerminalInjectionSiteCompatibility!=-1 && firstOfTerminalInjectionSiteCompatibility<indexsecondTitle){
                endOfTerminalInjectionSiteCompatibility = st.indexOf("$$$", firstOfTerminalInjectionSiteCompatibility+4)-3;
                NeofaxTable[number][12] = st.substring(firstOfTerminalInjectionSiteCompatibility+4, endOfTerminalInjectionSiteCompatibility);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            //Terminal Injection Site Incompatibility:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfTerminalInjectionSiteIncompatibility;
            int endOfTerminalInjectionSiteIncompatibility;
            firstOfTerminalInjectionSiteIncompatibility = st.indexOf("$$$ Terminal Injection Site Incompatibility", indexFirstTitle);
            if (firstOfTerminalInjectionSiteIncompatibility!=-1 && firstOfTerminalInjectionSiteIncompatibility<indexsecondTitle){
                endOfTerminalInjectionSiteIncompatibility = st.indexOf("$$$", firstOfTerminalInjectionSiteIncompatibility+4)-3;
                NeofaxTable[number][13] = st.substring(firstOfTerminalInjectionSiteIncompatibility+4, endOfTerminalInjectionSiteIncompatibility);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

//            //Table:
//            /////////////////////////////////////////////////////////////////////////////////////////////
//            int firstOfTable;
//            int endOfTable;
//            firstOfTable = st.indexOf("$$$ Table", indexFirstTitle);
//            if (firstOfTable!=-1 && firstOfTable<indexsecondTitle){
//                endOfTable = st.indexOf("$$$", firstOfTable)-3;
//                NeofaxTable[number][14] = st.substring(firstOfTable, endOfTable);
//            }
//            /////////////////////////////////////////////////////////////////////////////////////////////

            //References:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfReferences;
            int endOfReferences;
            firstOfReferences = st.indexOf("$$$ References", indexFirstTitle);
            if (firstOfReferences!=-1 && firstOfReferences<indexsecondTitle){
                endOfReferences = st.indexOf("$$$", firstOfReferences+4)-3;
                NeofaxTable[number][15] = st.substring(firstOfReferences+4, endOfReferences);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            number++;
        }



        st = null;

        Parser tmp = new Parser();

        st = tmp.getText(new File("C:\\Users\\mhn19\\Downloads\\neofax.docx"));



        st = st.replaceAll("\\d{1,3}\n\nMicormedex NeoFax Essentials 2014\n\n", "");
        st = st.replaceAll("\nDose", "\n\\$\\$\\$ Dose");
        st = st.replaceAll("Title ", "\\$\\$\\$ Title ");
        st = st.replaceAll("\nReferences", "\n\\$\\$\\$ References");
        st = st.replaceAll("\nAdministration", "\n\\$\\$\\$ Administration");
        st = st.replaceAll("\nUses", "\n\\$\\$\\$ Uses");
        st = st.replaceAll("\nContraindications/Precautions", "\n\\$\\$\\$ Contraindications/Precautions");
        st = st.replaceAll("\nBlack Box Warning", "\n\\$\\$\\$ Black Box Warning");
        st = st.replaceAll("\nPharmacology", "\n\\$\\$\\$ Pharmacology");
        st = st.replaceAll("\nAdverse Effects\n", "\n\\$\\$\\$ Adverse Effects\n");
        st = st.replaceAll("\nMonitoring", "\n\\$\\$\\$ Monitoring");
        st = st.replaceAll("\nSpecial Considerations/Preparation", "\n\\$\\$\\$ Special Considerations/Preparation");
        st = st.replaceAll("\nSolution Compatibility", "\n\\$\\$\\$ Solution Compatibility");
        st = st.replaceAll("\nSolution Incompatibility", "\n\\$\\$\\$ Solution Incompatibility");
        st = st.replaceAll("\nTerminal Injection Site Compatibility", "\n\\$\\$\\$ Terminal Injection Site Compatibility");
        st = st.replaceAll("\nTerminal Injection Site Incompatibility", "\n\\$\\$\\$ Terminal Injection Site Incompatibility");
        st = st.replaceAll("\nTable", "\n\\$\\$\\$ Table");
        st = st.replaceAll("\n\n\n", "\n");
        st = st.replaceAll("\n\n\n", "\n");
        st = st.replaceAll("\\.\n\n", "\\.\n");


//       System.out.print(st);
//       System.exit(0);



        indexFirstTitle = 0;
        indexsecondTitle = 0;
        number = 1;


        //2
        while(true){

            indexFirstTitle = st.indexOf("$$$ Title", indexsecondTitle + 9);
            indexsecondTitle = st.indexOf("$$$ Title", indexFirstTitle + 9);

            if(indexFirstTitle == -1){
                break;
            }


            //Does:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfDose;
            int endOfDose;
            firstOfDose = st.indexOf("$$$ Dose", indexFirstTitle);
            if (firstOfDose!=-1 && firstOfDose<indexsecondTitle){
                endOfDose = st.indexOf("$$$", firstOfDose+4)-3;
                NeofaxTable[number][1] = st.substring(firstOfDose+4, endOfDose);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            //Administration:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfAdministration;
            int endOfAdministration;
            firstOfAdministration = st.indexOf("$$$ Administration", indexFirstTitle);
            if (firstOfAdministration!=-1 && firstOfAdministration<indexsecondTitle){
                if (number==59 || number==95){
                    endOfAdministration = st.indexOf("$$$", firstOfAdministration+4)-3;
                    NeofaxTable[number][2] = null;
                    NeofaxTable[number][2] = st.substring(firstOfAdministration+4, endOfAdministration);
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            //Pharmacology:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfPharmacology;
            int endOfPharmacology;
            firstOfPharmacology = st.indexOf("$$$ Pharmacology", indexFirstTitle);
            if (firstOfPharmacology!=-1 && firstOfPharmacology<indexsecondTitle){
                if (number==90 || number==109 || number==116){
                    endOfPharmacology = st.indexOf("$$$", firstOfPharmacology+4)-3;
                    NeofaxTable[number][6] = null;
                    NeofaxTable[number][6] = st.substring(firstOfPharmacology+4, endOfPharmacology);
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            //Monitoring:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfMonitoring;
            int endOfMonitoring;
            firstOfMonitoring = st.indexOf("$$$ Monitoring", indexFirstTitle);
            if (firstOfMonitoring!=-1 && firstOfMonitoring<indexsecondTitle){
                if (number==7 || number==60 || number==79 || number==100 || number==107 || number==124 || number==180){
                    endOfMonitoring = st.indexOf("$$$", firstOfMonitoring+4)-3;
                    NeofaxTable[number][8] = null;
                    NeofaxTable[number][8] = st.substring(firstOfMonitoring+4, endOfMonitoring);
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            //Special Considerations/Preparation:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfSpecialConsiderationsPreparation;
            int endOfSpecialConsiderationsPreparation;
            firstOfSpecialConsiderationsPreparation = st.indexOf("$$$ Special Considerations/Preparation", indexFirstTitle);
            if (firstOfSpecialConsiderationsPreparation!=-1 && firstOfSpecialConsiderationsPreparation<indexsecondTitle){
                if (number==81 || number==95){
                    endOfSpecialConsiderationsPreparation = st.indexOf("$$$", firstOfSpecialConsiderationsPreparation+4)-3;
                    NeofaxTable[number][9] = null;
                    NeofaxTable[number][9] = st.substring(firstOfSpecialConsiderationsPreparation+4, endOfSpecialConsiderationsPreparation);
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////


            //Table:
            /////////////////////////////////////////////////////////////////////////////////////////////
            int firstOfTable;
            int endOfTable;
            firstOfTable = st.indexOf("$$$ Table", indexFirstTitle);
            if (firstOfTable!=-1 && firstOfTable<indexsecondTitle){
                if (number==63 || number==154 || number==167 || number==189){
                    endOfTable = st.indexOf("$$$", firstOfTable+4)-3;
                    NeofaxTable[number][14] = null;
                    NeofaxTable[number][14] = st.substring(firstOfTable+4, endOfTable);
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////

            number++;
        }
        /*
  CREATE TABLE `info` (
  `Title` mediumtext,
  `Dose` mediumtext,
  `Administration` mediumtext,
  `Uses` mediumtext,
  `Contraindications` mediumtext,
  `BlackBoxWarning` mediumtext,
  `Pharmacology` mediumtext,
  `AdverseEffects` mediumtext,
  `Monitoring` mediumtext,
  `Preparation` mediumtext,
  `SolutionCompatibility` mediumtext,
  `SolutionIncompatibility` mediumtext,
  `TerminalInjectionSiteCompatibility` mediumtext,
  `TerminalInjectionSiteIncompatibility` mediumtext,
  `Tables` mediumtext,
  `Reference` mediumtext
 ) ENGINE=MyISAM DEFAULT CHARSET=latin1;
        */
        try{
            Class.forName("org.sqlite.JDBC");  // initialise the driver
            String url ="jdbc:sqlite:C:/Users/mhn19/Downloads/neofax.sqlite";
            Connection con = DriverManager.getConnection(url);
            // drop tables
            String sql = "DROP TABLE IF EXISTS info";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            // create table
            String Create = "CREATE TABLE `info` (\n" +
                    "  `Title` mediumtext,\n" +
                    "  `Dose` mediumtext,\n" +
                    "  `Administration` mediumtext,\n" +
                    "  `Uses` mediumtext,\n" +
                    "  `Contraindications` mediumtext,\n" +
                    "  `BlackBoxWarning` mediumtext,\n" +
                    "  `Pharmacology` mediumtext,\n" +
                    "  `AdverseEffects` mediumtext,\n" +
                    "  `Monitoring` mediumtext,\n" +
                    "  `Preparation` mediumtext,\n" +
                    "  `SolutionCompatibility` mediumtext,\n" +
                    "  `SolutionIncompatibility` mediumtext,\n" +
                    "  `TerminalInjectionSiteCompatibility` mediumtext,\n" +
                    "  `TerminalInjectionSiteIncompatibility` mediumtext,\n" +
                    "  `Tables` mediumtext,\n" +
                    "  `Reference` mediumtext\n" +
                    " );";

            stmt.executeUpdate(Create);


            Statement state = con.createStatement();
            String query = "insert into info(Title,Dose,Administration,Uses,Contraindications,BlackBoxWarning,Pharmacology,AdverseEffects,Monitoring,Preparation,SolutionCompatibility,SolutionIncompatibility,"
                    + "TerminalInjectionSiteCompatibility,TerminalInjectionSiteIncompatibility,Tables,Reference)"+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStmt = con.prepareStatement(query);

            for(int i=1;i<196;i++){
                preparedStmt.setString (1,NeofaxTable[i][0] );
                preparedStmt.setString (2,NeofaxTable[i][1] );
                preparedStmt.setString (3,NeofaxTable[i][2] );
                preparedStmt.setString (4,NeofaxTable[i][3] );
                preparedStmt.setString (5,NeofaxTable[i][4] );
                preparedStmt.setString (6,NeofaxTable[i][5] );
                preparedStmt.setString (7,NeofaxTable[i][6] );
                preparedStmt.setString (8,NeofaxTable[i][7] );
                preparedStmt.setString (9,NeofaxTable[i][8] );
                preparedStmt.setString (10,NeofaxTable[i][9] );
                preparedStmt.setString (11,NeofaxTable[i][10] );
                preparedStmt.setString (12,NeofaxTable[i][11] );
                preparedStmt.setString (13,NeofaxTable[i][12] );
                preparedStmt.setString (14,NeofaxTable[i][13] );
                preparedStmt.setString (15,NeofaxTable[i][14] );
                preparedStmt.setString (16,NeofaxTable[i][15] );
                preparedStmt.execute();
            }
            con.close();
        }
        catch(Exception e)
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }



//
//        for (int i=0 ; i < 16; i++) {
//
////            if (NeofaxTable[i][14] != null){
////                System.out.println(i);
////            }
//
//            //System.out.println(i);
//            System.out.println(NeofaxTable[2][i]);
//            System.out.println("--------------------------------------------------------------------------------------------------------");
//
//        }

    }

    public  String getText(File file)
    {
        String st ="";
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            POITextExtractor extractor;
            XWPFDocument document = new XWPFDocument(fis);
            extractor = new XWPFWordExtractor(document);
            st = extractor.getText();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }
}
