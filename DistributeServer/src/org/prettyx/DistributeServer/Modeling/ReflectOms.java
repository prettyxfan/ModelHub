// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer.Modeling;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.sun.org.apache.bcel.internal.util.ClassLoader;

public class ReflectOms extends ClassLoader{

        /**
         * name class 类的文件名
         */
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] datas = loadClassData(name);
            return defineClass(name, datas, 0, datas.length);
        }

        // 指定文件目录
        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }


        protected byte[] loadClassData(String name)
        {
            FileInputStream fis = null;
            byte[] datas = null;
            try
            {
                fis = new FileInputStream(location + name + ".class");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int b;
                while( (b=fis.read())!=-1 )
                {
                    bos.write(b);
                }
                datas = bos.toByteArray();
                bos.close();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(fis != null)
                    try
                    {
                        fis.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
            }
            return datas;

        }

    public static void main(String[] args){
    //实例扩展类
    ReflectOms clod=new ReflectOms();

    //指定java class 文件目录
    clod.setLocation("E:/service/");

    try {
        //调用 通过字节流生产java类
        Class cl=clod.findClass("Test");

        //这里是调用带参数的方法，参数是数组对象
        Method method	=cl.getMethod("test2",new Class[]{String [].class});
        //如果这里 调用的是 实例方法 和以上的一样，只是后面调用Method的invoke不一样


        System.out.println(method.getName());


        method.invoke(cl, new Object[]{new String []{}});
        //这里的cl 是 生产的类，为什么这里没有  cl.newInstance() ,因为调用的test2 是静态方法，如果不是静态方法，需要用一下的
        //method.invoke(cl.newInstance(), new Object[]{new String []{}});


    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SecurityException e) {
        e.printStackTrace();
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    }
}