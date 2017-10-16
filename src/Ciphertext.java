/**
 * Created by Timmi on 12-Oct-17.
 */
class Ciphertext {
    private String ciphertext;
    private int parent1, parent2;

    Ciphertext(String a){
        ciphertext=a;
    }
    Ciphertext(String ciphertext,int parent1, int parent2){
        this.ciphertext=ciphertext;
        this.parent1=parent1;
        this.parent2=parent2;
    }
    String xor(Ciphertext ciphertext){
        StringBuilder stringBuilder=new StringBuilder();
        int size;
        if(ciphertext.getCiphertext().length()<this.ciphertext.length()){
            size=ciphertext.getCiphertext().length();
        }else{
            size=this.ciphertext.length();
        }
        for (int i=0;i<size;i++){
            if(this.ciphertext.charAt(i) ==' '){
                stringBuilder.append(' ');
            }else{
                switch (this.ciphertext.charAt(i)){
                    case '1':
                        switch (ciphertext.getCiphertext().charAt(i)){
                            case '1':
                                stringBuilder.append('0');
                                break;
                            case '0':
                                stringBuilder.append('1');
                                break;
                        }
                        break;
                    case '0':
                        switch (ciphertext.getCiphertext().charAt(i)){
                            case '1':
                                stringBuilder.append('1');
                                break;
                            case '0':
                                stringBuilder.append('0');
                                break;
                        }
                        break;
                }
            }
        }
        return stringBuilder.toString();
    }
    public String getCiphertext() {
        return ciphertext;
    }
    public int getParent1() {
        return parent1;
    }

    public int getParent2() {
        return parent2;
    }
}
