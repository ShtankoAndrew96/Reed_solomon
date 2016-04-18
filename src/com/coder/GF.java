package com.coder;


public class GF {
    public static int size;
    public GF(int size){
        this.size=size;
    }

    public static int Galua_Add(int a, int b){
        if((a+b)>size-1)
            return (a+b)%size;
        else
            return a+b;
    }

    public static int Galua_Sub(int a, int b){
        if(a>=b)
            return a-b;
        else {
            while (b != 0) {
                a--;
                b--;
                if (a < 0)
                    a = 6;
            }
            return a;
        }
    }

    public static int Galua_Mul(int a, int b){
        if(a*b>size-1)
            return (a * b) % size;
        else
            return a*b;

    }

    public static int Galua_Div(int a, int b){
        int c;
        int scal=1;
        for (int i=2; i<50; i++){
            if((a*i)%((b*i)%size)==0){
                scal = i;
                break;
            }
        }
        c = (a*scal)/((b*scal)%size);
        if (c>size-1){
            return c%size;
        }
        else
            return c;
    }

    public static int Galua_Pow(int a, int pow){
        if((int)Math.pow(a,pow)>size-1)
            return (int)Math.pow(a,pow)%size;
        else
            return (int)Math.pow(a,pow);
    }
}
