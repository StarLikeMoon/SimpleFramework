package org.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * 与类相关的工具类
 */
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    /**
     * 获取包名路径下的类集合，通过类加载器
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> extractPackageClass(String packageName) {
        // 1.获取到类加载器
        ClassLoader classLoader = getClassLoder();
        // 2.利用ClassLoader中的getResource来获取资源的URL地址
        // 2.1 将packageName中的.替换成/
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        // 判断空值
        if (url == null) {
            log.warn("unable to retrieve anything from package" + packageName);
            return null;
        }
        // 3.依据不同的资源类型，采用不同的方式获取资源的集合
        Set<Class<?>> classSet = null;
        // 过滤出文件的类型
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<>();
            // 获取到package实际路径，后续就可以遍历该目录及其子目录下的文件来获取类名了
            File packageDirectory = new File(url.getPath());
            // 提取资源
            extractClassFile(classSet, packageDirectory, packageName);
        }
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有Class文件（包括子package中的文件）
     *
     * @param emptyClassSet 装载目标类的集合
     * @param fileSource    文件或者目录
     * @param packageName   包名
     */
    private static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName) {
        //1.递归的终止条件，找到了文件而不是文件夹，就停止
        if (!fileSource.isDirectory()) {
            return;
        }
        // 如果是一个文件夹，就调用listFiles方法获取该文件夹下的文件或者文件夹，可以传入一个过滤器来过滤需要的文件
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // 需要文件夹，提取出来
                if (file.isDirectory()) {
                    return true;
                } else {
                    // 提取class文件，先获取文件的绝对值路径，判断其结尾是不是class
                    String absoluteFilePath = file.getAbsolutePath();
                    if (absoluteFilePath.endsWith("class")) {
                        // 若是class文件，就创建对象加载进集合
                        addToClassSet(absoluteFilePath);
                    }
                }
                // 由于class文件加载后就可以不用了，这里直接和其他不需要的文件一样return false直接丢弃
                return false;
            }

            /**
             * 根据class文件的绝对值路径，获取并生成class对象，并放入classSet中(内部类可以使用外部的参数)
             * @param absoluteFilePath
             */
            private void addToClassSet(String absoluteFilePath) {
                //1.从class文件的绝对值路径中提取出包含了package的类名
                // 由于操作系统不同，这里使用File.separator来获取当前操作系统的文件分隔符
                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                // 从absoluteFilePath中提取出全限定类名(内部类可以使用外部的参数)
                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                // 去掉.class
                className = className.substring(0, className.lastIndexOf("."));

                //2.通过反射机制获取对应的class对象并加入到classSet中
                Class targetClass = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });



        // 递归查询每个文件夹中的文件
        if (files != null) {
            for (File file : files) {
                // 递归调用
                extractClassFile(emptyClassSet, file, packageName);
            }
        }
    }

    /**
     * 获取Class对象，利用反射机制
     *
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error: ", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取目标类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoder() {
        // 程序是通过线程来执行的，获取执行当前该方法的线程，就能获取到资源信息
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 实例化class，利用反射，这里为了实现简单，以无参构造函数为例，不支持传参的类的实例化，支持私有的构造函数
     *
     * @param clazz      Class
     * @param <T>        类的实例
     * @param accessible 是否创建出私有构造函数的类的实例
     * @return
     */
    public static <T> T newInstace(Class<?> clazz, boolean accessible) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 利用反射设置类的成员变量属性值，即注入操作
     *
     * @param field      要注入的成员变量
     * @param target     类的实例，注入进哪个类
     * @param value      要注入的成员变量的实例
     * @param accessible 是否允许设置私有类型的成员变量
     */
    public static void setField(Field field, Object target, Object value, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("setField error", e);
            throw new RuntimeException(e);
        }
    }

}
