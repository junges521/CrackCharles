package cn.wjdiankong.hackcharles;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class CrackCharles {

    private final static String PKGNAME = "com.xk72.charles";
    private final static String CLASSNAME = "kKPk";
    private final static String JAR_DIR = "E:/GitHub/CrackCharles/";//�滻�����Ŀ¼
    private final static String JAR_NAME = "charles.jar";//�滻����ı���Ŀ¼

    public static void main(String[] args) throws Exception {

        //�����ԣ���������������һ��ִ�У���ֿ�ִ�У������ʧ��
        //����һ�� ��ִ�У�crackCharlesJar����������2����ִ�� execmd()�������ɣ�
        //����������cmd��ʹ�����������jar uvf charles.jar com/xk72/charles/kKPk.class

//      crackCharlesJar();
        execmd();
    }

    private static void execmd() throws Exception {

        String classPath = PKGNAME.replace(".", "/") + "/" + CLASSNAME + ".class";
        Process process = Runtime.getRuntime().exec("jar uvf " + JAR_NAME + " "  + classPath);
        System.out.println("jar uvf " + JAR_NAME + " " + classPath);
        int status = process.waitFor();
        if (status == 0) {
            System.out.println("ִ�гɹ���status�� " + status);
        } else {
            System.out.println("ִ��ʧ�ܣ�status�� " + status);
        }
    }

    private static void crackCharlesJar() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(JAR_DIR + JAR_NAME); //�����¼�
            }
        }).map(new Function<String, byte[]>() { //����jar�ļ����������֮������ֽ�������
            @Override
            public byte[] apply(String jarPath) throws Exception {
                ClassPool classPool = ClassPool.getDefault();
                classPool.insertClassPath(jarPath);
                CtClass ctClass = classPool.get(PKGNAME + "." + CLASSNAME);
                CtMethod ctMethod = ctClass.getDeclaredMethod("lcJx", null);
                ctMethod.setBody("{return true;}");
                ctMethod = ctClass.getDeclaredMethod("JZlU", null);
                ctMethod.setBody("{return \"Charles is success for crack !!!!!\";}");
                return ctClass.toBytecode();
            }
        }).map(new Function<byte[], String>() { // �����ֽ������飬�����ļ�������ļ�·��
            @Override
            public String apply(byte[] byteArray) throws Exception {
                String classPath = PKGNAME.replace(".", "/") + "/";
                File dirFile = new File(JAR_DIR + classPath + CLASSNAME + ".class");
                if (!dirFile.getParentFile().exists()) {
                    dirFile.getParentFile().mkdirs();
                }
                FileOutputStream output = new FileOutputStream(dirFile);
                output.write(byteArray);
                output.flush();
                output.close();
                return dirFile.getAbsolutePath();
            }
        }).map(new Function<String, Integer>() { //����class·����jar uvf�����滻jar
            @Override
            public Integer apply(String s) throws Exception {
                //                String classPath = PKGNAME.replace(".", "/") + "/" + CLASSNAME + ".class";
                //                Process process = Runtime.getRuntime().exec("jar uvf " + JAR_DIR + JAR_NAME + " " + JAR_DIR + classPath);
                //                 System.out.println("jar uvf " + JAR_DIR + JAR_NAME + " " + JAR_DIR + classPath);
                //                int status = process.waitFor();
                return 0;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer status) throws Exception {
                if (status == 0) {
                    System.out.println("ִ�гɹ���status�� " + status);
                } else {
                    System.out.println("ִ��ʧ�ܣ�status�� " + status);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("error:" + throwable.toString());
            }
        });
    }

}
