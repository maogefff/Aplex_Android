package com.aplex.aplexboottest;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShellExecute {
    /*
     * args[0] : shell 命令  如"ls" 或"ls -1";
     * args[1] : 命令执行路径  如"/" ;
     */
    public static String execute ( String command, String directory)
            throws IOException {

        // check the arguments
        if (null == command) {
            return "";
        }
        if (command.trim().equals("")) {
            return "";
        }

        if (null == directory || directory.trim().equals("")) {
            directory = "/";
        }

        String result = "" ;

        List<String> cmds = new ArrayList<String>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(command);

        try {
            ProcessBuilder builder = new ProcessBuilder(cmds);

            if ( directory != null )
                builder.directory ( new File ( directory ) ) ;
            builder.redirectErrorStream (true) ;
            Process process = builder.start ( ) ;

            //得到命令执行后的结果
            InputStream is = process.getInputStream ( ) ;
            byte[] buffer = new byte[1024] ;
            while ( is.read(buffer) != -1 ) {
                result = result + new String (buffer) ;
            }
            is.close ( ) ;
        } catch ( Exception e ) {
            e.printStackTrace ( ) ;
        }
        return result.trim() ;
    }

    /*
     * shell 命令  如"ls" 或"ls -1";
     */
    public static String execute (String command) throws IOException {

        // check the arguments
        if (null == command) {
            return "";
        }
        if (command.trim().equals("")) {
            return "";
        }

        return execute(command, "/");
    }

    public static boolean deviceExist( String pattern) throws IOException{

        // check the arguments
        if (null == pattern) {
            return false;
        }
        if (pattern.trim().equals("")) {
            return false;
        }

        return execute("dmesg | grep " + pattern).length() > 0;
    }
}
