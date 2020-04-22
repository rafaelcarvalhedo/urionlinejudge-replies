import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;

public class Main {

    private static Double[] values = new Double[]{100.0,50.0,20.0,10.0,5.0,2.0,1.0, 0.50, 0.25, 0.10, 0.05,0.01};

    public static void main(final String[] args) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String amountStr = reader.readLine();
        try {
            HashMap<Double,Integer> notesAndCoins = new HashMap<>();
            BigDecimal remainingAmount = new BigDecimal(amountStr).setScale(2,BigDecimal.ROUND_DOWN);
            StringBuilder outputCoin = new StringBuilder();
            StringBuilder outputNote = new StringBuilder();
            for (Double noteOrCoin : values) {
                int qty = 0;
                BigDecimal value = BigDecimal.valueOf(noteOrCoin).setScale(2);
                if(value.compareTo(remainingAmount) <= 0){
                     qty = remainingAmount.divide(value).intValue();
                     remainingAmount = remainingAmount.remainder(value);
                     notesAndCoins.put(value.doubleValue(),qty);
                 }

                if(value.compareTo(BigDecimal.ONE) > 0){
                    outputNote.append(String.format("\n%s nota(s) de R$ %s",qty,value));
                } else {
                    outputCoin.append(String.format("\n%s moeda(s) de R$ %s",qty,value));
                }
            }
           System.out.println("NOTAS:".concat(outputNote.toString()).concat("\nMOEDAS:").concat(outputCoin.toString()));
        } catch (NumberFormatException err) {
            System.out.println("Non valid input!");
        }
    }

}
