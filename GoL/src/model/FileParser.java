package model;

/**
 * Created by Truls on 18/01/16.
 */

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An utility class to make the link between a RLE file
 * and an integers array.
 */
public class FileParser {

    /**
     * Read a RLE file and convert it into an integers array.
     *
     * @param file the File to write into
     * @return the integers array produced from the file
     */


    static public boolean[][] read(File file){

        if(file.toString().endsWith(".cells")){
            return readPlainText(file);
        }
        else if(file.toString().endsWith(".rle")){
            return readRLE(file);
        }
        else if(file.toString().endsWith(".lif")){
            return readLife(file);
        }
        return null;
    }

    private static boolean[][] readLife(File file) {


        List<String>list = null;
        try {
            list = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }



        if(list.get(0).contains("Life 1.05")){
            return life05(list);
        }
        else if(list.get(0).contains("Life 1.06")){

            return life06(list);
        }

        return null;
    }


    private static boolean[][] life05(List<String> list) {

        while (!list.get(0).startsWith("#P")){
            list.remove(0);
        }

        int width = 0;
        int height = 0;


        Pattern p = Pattern.compile("#P (.+) (.+)");
        Matcher m = p.matcher(list.get(0));

        if(!m.matches()){
            System.out.println("feil av enellerannen grunn");
        }

        int startPosX = Integer.parseInt(m.group(1));
        int startPosY = Integer.parseInt(m.group(2));

        int offSetX = 0;
        int offSetY = 0;

        int unknownInt = 0;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).startsWith("#P")){
                m = p.matcher(list.get(i));
                if(!m.matches()){
                    System.out.println("Didnt match");
                }
                offSetX = Integer.parseInt(m.group(1));
                offSetY = Integer.parseInt(m.group(2));
                unknownInt = i;
            }
            if(list.get(i).length() - startPosX + offSetX > width){
                width = list.get(i).length() - startPosX + offSetX;
            }
            if((i-unknownInt) - startPosY + offSetY > height){
                height = i - unknownInt - startPosY + offSetY;
            }
        }

        System.out.println("Height: "+height);
        System.out.println("Width: "+width);

        boolean[][] imp = new boolean[width][height];

        char c;

        int x = 0;
        int y = 0;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).startsWith("#P")){
                m = p.matcher(list.get(i));
                if(!m.matches()){
                    System.out.println("Didnt match");
                }
                offSetX = Integer.parseInt(m.group(1));
                offSetY = Integer.parseInt(m.group(2));
                x = offSetX - startPosX;
                y = offSetY - startPosY;
                System.out.println("x: "+x);
                System.out.println("y: "+y);

            }
            else{
                for(int j = 0; j < list.get(i).length(); j++){
                    c = list.get(i).charAt(j);

                    if(c == '.'){
                        imp[x][y] = false;
                        x++;
                    }
                    else if(c == '*'){
                        imp[x][y] = true;
                        x++;
                    }

                }
                y++;
                x = offSetX - startPosX;
            }
        }



        return imp;
    }




    private static boolean[][] life06(List<String> list) {

        while(list.get(0).startsWith("#")){
            list.remove(0);
        }
        Pattern p = Pattern.compile("(.+) (.+)");
        Matcher m;

        m = p.matcher(list.get(0));

        if(!m.matches()){

            return null;
        }
        int startPosX = Integer.parseInt(m.group(1));
        int startPosY = Integer.parseInt(m.group(2));


        int possibleHeight;
        int possibleWidth;

        int width = startPosX;
        int height = startPosY;

        for(int i =0; i < list.size(); i++){
            m = p.matcher(list.get(i));

            if(!m.matches()){
                return null;
            }
            possibleWidth = Integer.parseInt(m.group(1));
            possibleHeight = Integer.parseInt(m.group(2));

            if(possibleHeight > height){
                height = possibleHeight;
            }

            if(possibleWidth > width){
                width = possibleWidth;
            }
            else if(possibleWidth < startPosX){
                startPosX = possibleWidth;
            }

        }

        width-=startPosX;
        height-=startPosY;

        boolean[][] imp = new boolean[width+1][height+1];

        for(int i = 0; i < list.size(); i++){
            m = p.matcher(list.get(i));

            if(!m.matches()){
                return null;
            }
            imp[Integer.parseInt(m.group(1))-startPosX][Integer.parseInt(m.group(2))-startPosY] = true;
        }


        return imp;
    }

    private static boolean[][] readRLE(File file) {
        int width;
        int height;


        List<String>list = null;
        try {
            System.out.println(file.toPath().subpath(1, 2));
            list = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (list.get(0).startsWith("#")){
            list.remove(0);
        }

        Pattern p = Pattern.compile("^x = ([0-9]+), y = ([0-9]+), rule = (.+)$");
        Matcher m = p.matcher(list.get(0));

        if(!m.matches()){
            return null;
        }

        height = Integer.parseInt(m.group(1));

        width = Integer.parseInt(m.group(2));
        System.out.println("Height: "+height);
        System.out.println("width: "+width);

        boolean[][] imp = new boolean[height][width];

        list.remove(0);
        char c;
        int antall = 0;
        int x = 0;
        int y = 0;
        int teller = 0;

        for(int i = 0; i<list.size(); i++){
            for(int j = 0; j < list.get(i).length(); j++){

                c = list.get(i).charAt(j);
                System.out.println(antall);

                if(Character.isDigit(c)){
                    antall = antall*10 + (c - '0');
                }
                else if(c == 'b'){
                    if(antall == 0){
                        imp[x][y] = false;
                        x++;
                    }
                    else {
                        for(int k = 0; k<antall; k++){
                            imp[x][y] = false;
                            x++;
                        }
                        antall = 0;
                    }
                }
                else if(c == 'o'){
                    if(antall == 0){
                        imp[x][y] = true;
                        x++;
                    }
                    else {
                        for(int k = 0; k<antall; k++){
                            imp[x][y] = true;
                            x++;
                        }
                        antall = 0;
                    }
                }
                else if(c == '$'){

                    if(antall == 0){
                        y++;
                    }
                    else{
                        y+= antall;
                    }
                    antall = 0;

                    x=0;
                }
                else if(c == '!'){
                    return imp;
                }
            }
        }





      /*  for(int a = 0; a< list.size(); a++){
       //     System.out.println(list.get(a));
        }*/
        return null;

    }

    private static boolean[][] readPlainText(File file) {
        int width;
        int height;


        List<String> list = null;
        try {
            System.out.println(file.toPath().subpath(1, 2));
            list = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }



        while(list.get(0).startsWith("!")){
            list.remove(0);
        }


        height = list.size();
        width = 0;

        //Finner den lengste linjen

        for(int x = 0; x < list.size(); x++){
            if(list.get(x).length() > width){
                width = list.get(x).length();
            }
        }

        System.out.println(width+" " + height);

        boolean[][] imp = new boolean[width][height];

        for(int y = 0; y<height; y++){
            for(int x = 0; x<list.get(y).length();x++){

                if(list.get(y).charAt(x) == 'O'){
                    imp[x][y] = true;
                }
                else{
                    imp[x][y] = false;
                }
            }
        }

        return imp;
    }



}
