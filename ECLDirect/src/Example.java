

import java.util.ArrayList;
import org.hpccsystems.ecldirect.Column;
import org.hpccsystems.ecldirect.Dataset;
import org.hpccsystems.ecldirect.EclDirect;
import org.hpccsystems.ecldirect.Join;
import org.hpccsystems.ecldirect.Output;
import org.hpccsystems.ecldirect.Spray;

public class Example {
    public static void main(String[] args) throws Exception {
      
        //String ecl;
        //File file = new File("ecl/sample3.ecl");
        //ecl = FileUtils.readFileToString(file);
        
        Spray spray = new Spray();
        spray.setClusterName("mythor");
        spray.setFilePath("/var/lib/HPCCSystems/mydropzone/Person.csv");
        spray.setIpAddress("192.168.59.129");
        spray.setLogicalFileName("~in::person");
        spray.setFileType("variable");
//      
//        System.out.println(spray.ecl());
        
        //Person Dataset
        Dataset dsPerson = new Dataset();
        dsPerson.setLogicalFileName("~in::person");
        dsPerson.setName("personDS");
        
        ArrayList personRecordDef = new ArrayList();
        personRecordDef.add("string id");
        personRecordDef.add("string firstName");
        personRecordDef.add("string lastName");
        personRecordDef.add("string address");
        personRecordDef.add("string state");   
        personRecordDef.add("string city");
        personRecordDef.add("string zip");
        
        dsPerson.setRecordFormatList(personRecordDef);
        dsPerson.setRecordName("personRec");
        dsPerson.setFileType("CSV");
        
        Dataset dsPersonContact = new Dataset();
        dsPersonContact.setLogicalFileName("~in::person_contact");
        dsPersonContact.setName("personContactDS");
        
        ArrayList contactRecordDef = new ArrayList();
        contactRecordDef.add("string id");
        contactRecordDef.add("string phone_type");
        contactRecordDef.add("string phone_number");
        
        dsPersonContact.setRecordFormatList(contactRecordDef);
        dsPersonContact.setRecordName("personContactRec");
        dsPersonContact.setFileType("CSV");

       
        Join join = new Join();
        
        join.setName("personAndContactDS");
        join.setLeftRecordSet("personDS");
        join.setRightRecordSet("personContactDS");
        join.setJoinCondition("left.id=right.id");
        join.setJoinType("inner");


        Output output = new Output();
        output.setDefinitionName("personAndContactDS");

        String ecl = dsPerson.ecl() + dsPersonContact.ecl() + join.ecl() + output.ecl();
//        String ecl = spray.ecl();
        System.out.println(ecl);
        
        
        EclDirect eclDirect = new EclDirect("192.168.59.129", "thor");
        ArrayList dsList = eclDirect.execute(ecl);
        for (int i = 0; i < dsList.size(); i++) {
            
            ArrayList rowList = (ArrayList) dsList.get(i);
            System.out.println("Row:");
            for (int j = 0; j < rowList.size(); j++) {
                ArrayList columnList = (ArrayList) rowList.get(j);
                
                for (int k = 0; k < columnList.size(); k++) {
                    Column column = (Column) columnList.get(k);
                    System.out.print(column.getName() + "=" + column.getValue() + "|");
                }
                System.out.println("");
            }
        }
        
    }

}
