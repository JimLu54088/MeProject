package ntuSP17HW1;

public class CalculateScoreByExtractInHundredAndTenRunBatch {

    public static void main(String[] args) {

//        System.out.println("======= Start =======");

        AppImp appImp = new AppImp();
//        runfrom0to9999();

        String result = String.format("%04d", appImp.extractAndSquare(3025));
        System.out.println("result " + result);

//        System.out.println("======= End =======");

//        long score = appImp.retriveScore(241);
//
//        System.out.println("得分: " + score);


    }

    public static void runfrom0to9999() {

        AppImp appImp = new AppImp();

        for (int i = 0; i < 1000; i++) {

            if (i >= 240 && i <= 249) {
                continue;

            }


            long score = appImp.retriveScore(i);

            if (score >= 10) {
                System.out.println("the score of " + i + " is " + score);
            }


        }


    }

}
