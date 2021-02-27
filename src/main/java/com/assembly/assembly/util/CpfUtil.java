package com.assembly.assembly.util;

import static java.util.Objects.isNull;

public class CpfUtil {

    public static boolean isValidCpf(String cpf){
        if(isNull(cpf))
            return false;

        if (cpf.length() == 11 && !cpf.equals("00000000000") ) {
            int     d1, d2;
            int     digit1, digit2, rest;
            int     cpfDigit;
            String  nDigResult;
            d1 = d2 = 0;
            digit1 = digit2 = rest = 0;
            for (int n_Count = 1; n_Count < cpf.length() -1; n_Count++) {
                cpfDigit = Integer.parseInt(cpf.substring(n_Count - 1, n_Count));
                //--------- Multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
                d1 = d1 + ( 11 - n_Count ) * cpfDigit;
                //--------- Para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
                d2 = d2 + ( 12 - n_Count ) * cpfDigit;
            }
            //--------- Primeiro rest da diviso por 11.
            rest = (d1 % 11);
            //--------- Se o resultado for 0 ou 1 o digito  0 caso contrrio o digito  11 menos o resultado anterior.
            if (rest >= 2)
                digit1 = 11 - rest;
            d2 += 2 * digit1;
            //--------- Segundo rest da diviso por 11.
            rest = (d2 % 11);
            if(rest >= 2)
                digit2 = 11 - rest;
            //--------- Digito verificador do CPF que est sendo validado.
            String nDigVerific = cpf.substring (cpf.length()-2, cpf.length());
            //--------- Concatenando o primeiro rest com o segundo.
            nDigResult = String.valueOf(digit1) + digit2;
            //--------- Comparar o digito verificador do cpf com o primeiro rest + o segundo rest.
            return nDigVerific.equals(nDigResult);
        }

        return false;
    }

    public static String removeSpecialCharacters(String cpf){
        if(cpf != null && cpf.length() > 0 ){
            return cpf.replace(".", "").replace("/","").replace("-", "");
        }else {
            return cpf;
        }
    }
}
