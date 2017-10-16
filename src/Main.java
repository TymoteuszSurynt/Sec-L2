import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Timmi on 12-Oct-17.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList <Ciphertext> ciphertexts=new ArrayList<Ciphertext>();
        ArrayList <Ciphertext> xorCiphertexts=new ArrayList<Ciphertext>();
        FileReader in;
        BufferedReader br;
        StringBuilder sb;
        Scanner sc = new Scanner(System.in);
        String c;
        int option,option2,option3;
        for (int i=0;i<20;i++){
            try {
                in=new FileReader("C:\\Users\\Timmi\\Desktop\\Sec\\SecL1\\K\\k"+Integer.toString(i+1)+".txt");
                br=new BufferedReader(in);
                sb=new StringBuilder();
                while ((c = br.readLine()) != null) {
                    sb.append(c);
                }
                ciphertexts.add(new Ciphertext(sb.toString()));
                System.out.println("File"+Integer.toString(i+1)+" loaded");
                br.close();
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        while(true){
            System.out.println("What now? 1-xor two ciphertexts 2-Show saved xored ciphers 3-Show saved not xored ciphertexts 9-Exit");
            try{
                option=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                option=-1;
                System.out.println("Wrong number");
            }
            if (option==1){
                System.out.println("Choose first ciphertext");
                try{
                    option=Integer.parseInt(sc.nextLine());
                    System.out.println("Choose second ciphertext");
                    option2=Integer.parseInt(sc.nextLine());
                    c=ciphertexts.get(option-1).xor(ciphertexts.get(option2-1));
                    System.out.println("Result:");
                    System.out.println(c);
                    System.out.println("Save? 1-Yes 2-No");
                    option3=Integer.parseInt(sc.nextLine());
                    if(option3==1){
                        xorCiphertexts.add(new Ciphertext(c,option,option2));
                    }

                }catch (Exception e){
                    System.out.println("Wrong number");
                }
            }else if(option==2){
                option=1;
                for (Ciphertext ciphertext:xorCiphertexts) {
                    System.out.println("Ciphertext "+Integer.toString(option)+" From "+Integer.toString(ciphertext.getParent1())+" and "+Integer.toString(ciphertext.getParent2())+"\n");
                    System.out.println(ciphertext.getCiphertext()+"\n");
                    option++;

                }
            }else if(option==3){
                System.out.println("Which? 1-20 ciphertext 21-all");
                try {
                    option3 = Integer.parseInt(sc.nextLine());
                    if (option3 == 21) {
                        option = 1;
                        for (Ciphertext ciphertext : ciphertexts) {
                            System.out.println("Ciphertext " + Integer.toString(option) + "\n");
                            System.out.println(ciphertext.getCiphertext() + "\n");
                            option++;

                        }
                    }else{
                        System.out.println("Ciphertext " + Integer.toString(option3) + "\n");
                        System.out.println(ciphertexts.get(option3-1).getCiphertext() + "\n");
                    }
                }catch (Exception e){
                    System.out.println("Wrong number");
                }
            }
            else if(option==9){
                break;
            }
        }

    }
}
