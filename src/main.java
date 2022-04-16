import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class main {

    public static void main(String args[]) throws FileNotFoundException {
        Apriori apriori = new Apriori();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Minimum Support");
        int mnSup =scanner.nextInt();
        System.out.println("Enter Minimum Confidence");
        float mnConf =scanner.nextFloat();
        apriori.setMinimumSupport(mnSup);
        apriori.setMinimumConfidence(mnConf);
        Map<String,Integer> mp1 = apriori.CreateCandidate1(apriori.file);
        Map<String,Integer> mp2 = apriori.excludeUnderMinimumSupport(mp1);
        Map<String,Integer> mp3 = apriori.createCandidate2(mp2);
        Map<String,Integer> mp4 = apriori.createCandidate3(mp3);
        Map<String,Integer> mp5 = apriori.removeDuplicates(mp4);
        apriori.printFrequencySets(mp5);
        System.out.println("-----------------------------------------------------------------------------------------");
        Map<String,Float>mp6 = apriori.getAllConfidence(mp5);
        apriori.printConfidenceRules(mp6);
    }
}
