package com.coder;

import static com.coder.GF.*;

public class Main {
    public static int[] simbol = {5,1,0,0,0,0};
    public static int[] code = new int[6];
    public static int[] simbol2 = new int[6];
    public static int[] sindrom = new int[4];
    public static int[][] teplits = new int[3][3];
    public static int[] result = new int[3];
    public static int num=6;
    public static void main(String[] args) {
        int n=7;
        GF gf = new GF(n);
        System.out.print("Simbol ");
        for (int i=0; i<num; i++){
            System.out.print(simbol[i]+" ");
        }
        System.out.println();
        /*********************************/
        /*************CODER***************/
        /*********************************/
        System.out.println("Coding...");
        System.out.print("Code: ");
        IDFT(simbol, code);
        for(int i=0; i<num; i++){
            System.out.print(code[i]+" ");
        }
        System.out.println();
        /**********Mistake add***********/
        System.out.print("Add mistake: ");
        int[] mistake= {0,0,6,0,6,0};
        for(int i=0; i<num; i++){
            System.out.print(mistake[i]+" ");
        }
        System.out.println();
        Create_Mistake(mistake);
        System.out.print("Code with mistake: ");
        for(int i=0; i<num; i++){
            System.out.print(code[i]+" ");
        }
        System.out.println();
        /*********************************/
        /************DECODER**************/
        /*********************************/
        DFT(code, simbol2);
        System.out.println("Decoding...");
        System.out.print("Decoded simbol: ");
        for(int i=0; i<num; i++){
            System.out.print(simbol2[i]+" ");
        }
        System.out.println();
        int j=0;
        int a=0;
        for(int i=2; i<num; i++){
            sindrom[j]=simbol2[i];
            if(sindrom[j]==0)
                a++;
            j++;
        }
        if(a!=4) {
            System.out.print("Sindrom: ");
            for(int i=0; i<4; i++){
                System.out.print(sindrom[i]+" ");
            }
            System.out.println();
            build_Teplits_matrix();
            System.out.println("Search bits with mistake: ");
            Berlekamp_Messi();
            Normalisation();
            Ferni();
        }
    }

    public static int getPol_IDFT(int[] simbol,int x){
        return Galua_Add(simbol[0], Galua_Add(Galua_Mul(simbol[1], x), Galua_Add(Galua_Mul(simbol[2], Galua_Pow(x, 2)), Galua_Add(Galua_Mul(simbol[3],
                Galua_Pow(x, 3)), Galua_Add(Galua_Mul(simbol[4], Galua_Pow(x, 4)),Galua_Mul(simbol[5], Galua_Pow(x, 5)))))));
    }

    public static int getPol_DFT(int[] code,int x){
        return Galua_Div(Galua_Add(code[0], Galua_Add(Galua_Div(code[1], Galua_Pow(x, 1)), Galua_Add(Galua_Div(code[2], Galua_Pow(x, 2)), Galua_Add(Galua_Div(code[3],
                Galua_Pow(x, 3)), Galua_Add(Galua_Div(code[4], Galua_Pow(x, 4)), Galua_Div(code[5], Galua_Pow(x, 5))))))), 6);
    }


    public static void IDFT(int[] simbol,int[] code){
        for (int i=0; i<num; i++){
            code[i]=getPol_IDFT(simbol,Galua_Pow(5, i));
        }
    }

    public static void DFT(int[] code, int[] simbol2){
        for (int i=0; i<num; i++){
            simbol2[i] = getPol_DFT(code,Galua_Pow(5, i));
        }
    }

    public static void Create_Mistake(int [] mistake){
        for(int i=0; i<num; i++){
            code[i] = Galua_Add(code[i], mistake[i]);
        }
    }

    public static void build_Teplits_matrix() {
        for(int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                teplits[i][j]=-1;
            }
        }
        int i = 2;
        int j = 0;
        for (int k = 0; k < 4; k++) {
            if(i!=0){
                int h = i;
                int w = j;
                for(h=i,w=j;h<3;h++,w++){
                    teplits[h][w]=sindrom[k];
                }
                i--;
            }else{
                int h = i;
                int w = j;
                for(h=i,w=j;w<3;h++,w++){
                    teplits[h][w]=sindrom[k];
                }
                j++;
            }
        }
    }

    public static void Berlekamp_Messi(){
        int[] G = {1,-1,-1};
        int one = G[0]*teplits[2][0];
        int[] G1 ={1,0};
        int[] G1_result={0,0};
        int[] G2_result={0,0,0};
        for(int j=0; j<2; j++){
                for(int k =0; k<2; k++){
                    G1_result[j]=Galua_Add(G1_result[j],Galua_Mul(G1[k],teplits[k+1][j]));
                }
        }
        int[] G1_result1 = {0, 0};
        if(G1_result[0]!=0) {
            int ch = G1_result[0];
            int[] G1_1 = {0, 1};
            for (int i = 0; i < 2; i++) {
                G1_result[i] = Galua_Sub(G1[i], Galua_Mul(Galua_Div(ch, one), G1_1[i]));
            }

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    G1_result1[j] = Galua_Add(G1_result1[j], Galua_Mul(G1_result[k], teplits[k+1][j]));
                }
            }
        }else{
            G1_result1=G1_result;
            G1_result=G1;
        }

            for (int j = 0; j < 2; j++) {
                G[j] = G1_result[j];
            }
            G[2] = 0;
        if(G1_result1[0]==0&&G1_result1[1]==0) {
            result=G;
        }else {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    G2_result[j] = Galua_Add(G2_result[j], Galua_Mul(G[k], teplits[k][j]));
                }
            }
            result = G2_result;
            if (G2_result[0] != 0) {
                int ch1 = G2_result[0];
                int[] G2_1 = {0, 0, 1};
                for (int i = 0; i < 3; i++) {
                    G2_result[i] = Galua_Sub(G[i], Galua_Mul(Galua_Div(ch1, one), G2_1[i]));
                }

                int[] G2_result1 = {0, 0, 0};
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        G2_result1[j] = Galua_Add(G2_result1[j], Galua_Mul(G2_result[k], teplits[k][j]));
                    }
                }
                result = G2_result;
                int[] G2_result2 = new int[3];
                if (G2_result1[1] != 0) {
                    int[] G2_2 = new int[3];
                    G2_2[0] = 0;
                    for (int i = 0; i < 2; i++) {
                        G2_2[i + 1] = G1_result[i];
                    }
                    int ch2 = G2_result1[1];
                    int two = G1_result1[1];
                    for (int i = 0; i < 3; i++) {
                        G2_result1[i] = Galua_Sub(G2_result[i], Galua_Mul(Galua_Div(ch2, two), G2_2[i]));
                    }
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            G2_result2[j] = Galua_Add(G2_result2[j], Galua_Mul(G2_result1[k], teplits[k][j]));
                        }
                    }
                    result = G2_result1;
                }
            } else if (G2_result[1] != 0) {
                int[] G2_result1 = {0, 0, 0};
                int[] G2_2 = new int[3];
                G2_2[0] = 0;
                for (int i = 0; i < 2; i++) {
                    G2_2[i + 1] = G1_result[i];
                }
                int ch2 = G2_result[1];
                int two = G1_result1[1];
                for (int i = 0; i < 3; i++) {
                    G2_result[i] = Galua_Sub(G[i], Galua_Mul(Galua_Div(ch2, two), G2_2[i]));
                }
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        G2_result1[j] = Galua_Add(G2_result1[j], Galua_Mul(G2_result[k], teplits[k][j]));
                    }
                }
                result = G2_result;
            }
        }
        for(int i =0; i<num; i++){
            if(Galua_Add(result[0],Galua_Add(Galua_Mul(result[1],Galua_Pow(Galua_Pow(5,i),1)),Galua_Mul(result[2],Galua_Pow(Galua_Pow(5,i),2))))==0) {
                System.out.println("Mistake in " + i);
            }
        }
    }

    public static void Normalisation(){
        for(int i=0; i<num; i++){
            if(Galua_Mul(result[2],i)==1){
                for(int j=0; j<3; j++){
                    result[j]=Galua_Mul(result[j],i);

                }
                break;
            }
        }
    }

    public static void Ferni(){
        int[] F = new int[num];
        int[] mistake = new int[num];
        int[] simbol = new int[num];
        int[] code1 = new int[num];
        F[0]=-1;
        F[1]=-1;
        for(int i=2; i<num; i++){
            F[i]=sindrom[i-2];
        }
        //int svertka1= Galua_Add(Galua_Mul(F[4], result[0]), Galua_Add(Galua_Mul(F[3], result[1]), Galua_Mul(F[2],result[2])));
        //int svertka2= Galua_Add(Galua_Mul(F[5], result[0]), Galua_Add(Galua_Mul(F[4], result[1]), Galua_Mul(F[3],result[2])));
        F[1]=Galua_Sub(Galua_Sub(0,Galua_Mul(result[0],F[3])),Galua_Mul(result[1],F[2]));
        F[0]=Galua_Sub(Galua_Sub(0,Galua_Mul(result[0],F[2])),Galua_Mul(result[1],F[1]));
        IDFT(F, mistake);
        for(int i=0; i<num; i++){
            code1[i]=Galua_Sub(code[i],mistake[i]);
        }
        DFT(code1, simbol);
        System.out.print("Decoded simbol ");
        for (int i=0; i<num; i++){
            System.out.print(simbol[i]+" ");
        }
    }

}
