/**
 * 
 */
package jvm.base.classload;

import java.lang.reflect.Method;

/**
 * 关于反射调用方法的一个log: sun.reflect.GeneratedMethodAccessor1 from __JVM_DefineClass__
 * 
 * http://rednaxelafx.javaeye.com/blog/548536 
 * 
 * Sun的JDK是从1.4系开始采用这种优化的。 
 * 如注释所述，实际的MethodAccessor实现有两个版本，一个是Java实现的，另一个是native code实现的。
 * Java实现的版本在初始化时需要较多时间，但长久来说性能较好；
 * native版本正好相反，启动时相对较快，但运行时间长了之后速度就比不过Java版了。
 * 这是HotSpot的优化方式带来的性能特性，同时也是许多虚拟机的共同点：跨越native边界会对优化有阻碍作用，
 * 它就像个黑箱一样让虚拟机难以分析也将其内联，于是运行时间长了之后反而是托管版本的代码更快些。
 * 为了权衡两个版本的性能，Sun的JDK使用了“inflation”的技巧：让Java方法在被反射调用时，
 * 开头若干次使用native版，等反射调用次数超过阈值时则生成一个专用的MethodAccessor实现类，
 * 生成其中的invoke()方法的字节码，以后对该Java方法的反射调用就会使用Java版。 
 * 
 */
public class TestClassLoad {
    public static void main(String[] args) throws Exception {
        Class<?> clz = Class.forName("jvm.base.classload.A");
        Object o = clz.newInstance();
        Method m = clz.getMethod("foo", String.class);
        for (int i = 0; i < 16; i++) {
            m.invoke(o, Integer.toString(i));
        }
    }
}

/*

D:\study\tempProject\JavaLearn>javac -d classes -sourcepath base.src -classpath "." base.src/jvm/bas
e/classload/A.java

D:\study\tempProject\JavaLearn>javac -d classes -sourcepath base.src -classpath "." base.src/jvm/bas
e/classload/TestClassLoad.java

D:\study\tempProject\JavaLearn>cd classes

D:\study\tempProject\JavaLearn\classes>java -verbose:class jvm.base.classload.TestClassLoad
or D:\study\tempProject\JavaLearn\classes>java -verbose:class jvm.base.classload.TestClassLoad
or D:\study\tempProject\JavaLearn\classes>java -XX:+TraceClassLoading jvm.base.classload.TestClassLoad
[Loaded java.lang.Object from shared objects file]
[Loaded java.io.Serializable from shared objects file]
[Loaded java.lang.Comparable from shared objects file]
[Loaded java.lang.CharSequence from shared objects file]
[Loaded java.lang.String from shared objects file]
[Loaded java.lang.reflect.GenericDeclaration from shared objects file]
[Loaded java.lang.reflect.Type from shared objects file]
[Loaded java.lang.reflect.AnnotatedElement from shared objects file]
[Loaded java.lang.Class from shared objects file]
[Loaded java.lang.Cloneable from shared objects file]
[Loaded java.lang.ClassLoader from shared objects file]
[Loaded java.lang.System from shared objects file]
[Loaded java.lang.Throwable from shared objects file]
[Loaded java.lang.Error from shared objects file]
[Loaded java.lang.ThreadDeath from shared objects file]
[Loaded java.lang.Exception from shared objects file]
[Loaded java.lang.RuntimeException from shared objects file]
[Loaded java.security.ProtectionDomain from shared objects file]
[Loaded java.security.AccessControlContext from shared objects file]
[Loaded java.lang.ReflectiveOperationException from shared objects file]
[Loaded java.lang.ClassNotFoundException from shared objects file]
[Loaded java.lang.LinkageError from shared objects file]
[Loaded java.lang.NoClassDefFoundError from shared objects file]
[Loaded java.lang.ClassCastException from shared objects file]
[Loaded java.lang.ArrayStoreException from shared objects file]
[Loaded java.lang.VirtualMachineError from shared objects file]
[Loaded java.lang.OutOfMemoryError from shared objects file]
[Loaded java.lang.StackOverflowError from shared objects file]
[Loaded java.lang.IllegalMonitorStateException from shared objects file]
[Loaded java.lang.ref.Reference from shared objects file]
[Loaded java.lang.ref.SoftReference from shared objects file]
[Loaded java.lang.ref.WeakReference from shared objects file]
[Loaded java.lang.ref.FinalReference from shared objects file]
[Loaded java.lang.ref.PhantomReference from shared objects file]
[Loaded java.lang.ref.Finalizer from shared objects file]
[Loaded java.lang.Runnable from shared objects file]
[Loaded java.lang.Thread from shared objects file]
[Loaded java.lang.Thread$UncaughtExceptionHandler from shared objects file]
[Loaded java.lang.ThreadGroup from shared objects file]
[Loaded java.util.Dictionary from shared objects file]
[Loaded java.util.Map from shared objects file]
[Loaded java.util.Hashtable from shared objects file]
[Loaded java.util.Properties from shared objects file]
[Loaded java.lang.reflect.AccessibleObject from shared objects file]
[Loaded java.lang.reflect.Member from shared objects file]
[Loaded java.lang.reflect.Field from shared objects file]
[Loaded java.lang.reflect.Method from shared objects file]
[Loaded java.lang.reflect.Constructor from shared objects file]
[Loaded sun.reflect.MagicAccessorImpl from shared objects file]
[Loaded sun.reflect.MethodAccessor from shared objects file]
[Loaded sun.reflect.MethodAccessorImpl from shared objects file]
[Loaded sun.reflect.ConstructorAccessor from shared objects file]
[Loaded sun.reflect.ConstructorAccessorImpl from shared objects file]
[Loaded sun.reflect.DelegatingClassLoader from shared objects file]
[Loaded sun.reflect.ConstantPool from shared objects file]
[Loaded sun.reflect.FieldAccessor from shared objects file]
[Loaded sun.reflect.FieldAccessorImpl from shared objects file]
[Loaded sun.reflect.UnsafeFieldAccessorImpl from shared objects file]
[Loaded sun.reflect.UnsafeStaticFieldAccessorImpl from shared objects file]
[Loaded java.lang.Iterable from shared objects file]
[Loaded java.util.Collection from shared objects file]
[Loaded java.util.AbstractCollection from shared objects file]
[Loaded java.util.List from shared objects file]
[Loaded java.util.AbstractList from shared objects file]
[Loaded java.util.RandomAccess from shared objects file]
[Loaded java.util.Vector from shared objects file]
[Loaded java.lang.Appendable from shared objects file]
[Loaded java.lang.AbstractStringBuilder from shared objects file]
[Loaded java.lang.StringBuffer from shared objects file]
[Loaded java.lang.StackTraceElement from shared objects file]
[Loaded java.nio.Buffer from shared objects file]
[Opened D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.lang.Boolean from shared objects file]
[Loaded java.lang.Character from shared objects file]
[Loaded java.lang.Number from shared objects file]
[Loaded java.lang.Float from shared objects file]
[Loaded java.lang.Double from shared objects file]
[Loaded java.lang.Byte from shared objects file]
[Loaded java.lang.Short from shared objects file]
[Loaded java.lang.Integer from shared objects file]
[Loaded java.lang.Long from shared objects file]
[Loaded java.io.ObjectStreamField from shared objects file]
[Loaded java.util.Comparator from shared objects file]
[Loaded java.lang.String$CaseInsensitiveComparator from shared objects file]
[Loaded java.security.Guard from shared objects file]
[Loaded java.security.Permission from shared objects file]
[Loaded java.security.BasicPermission from shared objects file]
[Loaded java.lang.RuntimePermission from shared objects file]
[Loaded java.security.AccessController from shared objects file]
[Loaded java.lang.reflect.ReflectPermission from shared objects file]
[Loaded java.security.PrivilegedAction from shared objects file]
[Loaded sun.reflect.ReflectionFactory$GetReflectionFactoryAction from shared objects file]
[Loaded java.util.AbstractMap from shared objects file]
[Loaded java.util.WeakHashMap from shared objects file]
[Loaded java.lang.ref.ReferenceQueue from shared objects file]
[Loaded java.lang.ref.ReferenceQueue$Null from shared objects file]
[Loaded java.lang.ref.ReferenceQueue$Lock from shared objects file]
[Loaded java.util.Map$Entry from shared objects file]
[Loaded java.util.WeakHashMap$Entry from shared objects file]
[Loaded java.util.Collections from shared objects file]
[Loaded java.util.Set from shared objects file]
[Loaded java.util.AbstractSet from shared objects file]
[Loaded java.util.Collections$EmptySet from shared objects file]
[Loaded java.util.Collections$EmptyList from shared objects file]
[Loaded java.util.Collections$EmptyMap from shared objects file]
[Loaded java.util.Collections$SynchronizedMap from shared objects file]
[Loaded java.util.Collections$SetFromMap from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.util.Collections$SynchronizedCollection from shared objects file]
[Loaded java.util.Collections$SynchronizedSet from shared objects file]
[Loaded java.util.WeakHashMap$KeySet from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.util.HashMap from shared objects file]
[Loaded java.lang.ref.Reference$Lock from shared objects file]
[Loaded java.lang.ref.Reference$ReferenceHandler from shared objects file]
[Loaded java.security.cert.Certificate from shared objects file]
[Loaded java.util.Stack from shared objects file]
[Loaded sun.reflect.ReflectionFactory from shared objects file]
[Loaded java.lang.ref.Finalizer$FinalizerThread from shared objects file]
[Loaded java.util.Hashtable$Entry from shared objects file]
[Loaded java.nio.charset.Charset from shared objects file]
[Loaded java.nio.charset.spi.CharsetProvider from shared objects file]
[Loaded sun.nio.cs.FastCharsetProvider from shared objects file]
[Loaded sun.nio.cs.StandardCharsets from shared objects file]
[Loaded sun.util.PreHashedMap from shared objects file]
[Loaded sun.nio.cs.StandardCharsets$Aliases from shared objects file]
[Loaded sun.nio.cs.StandardCharsets$Classes from shared objects file]
[Loaded sun.nio.cs.StandardCharsets$Cache from shared objects file]
[Loaded java.lang.ThreadLocal from shared objects file]
[Loaded java.util.concurrent.atomic.AtomicInteger from shared objects file]
[Loaded sun.misc.Unsafe from shared objects file]
[Loaded java.lang.IncompatibleClassChangeError from shared objects file]
[Loaded java.lang.NoSuchMethodError from shared objects file]
[Loaded sun.reflect.Reflection from shared objects file]
[Loaded java.util.HashMap$Entry from shared objects file]
[Loaded java.lang.Math from shared objects file]
[Loaded java.util.HashMap$EntrySet from shared objects file]
[Loaded java.util.Iterator from shared objects file]
[Loaded java.util.HashMap$HashIterator from shared objects file]
[Loaded java.util.HashMap$EntryIterator from shared objects file]
[Loaded java.lang.Class$3 from shared objects file]
[Loaded java.lang.reflect.Modifier from shared objects file]
[Loaded sun.reflect.LangReflectAccess from shared objects file]
[Loaded java.lang.reflect.ReflectAccess from shared objects file]
[Loaded java.util.Arrays from shared objects file]
[Loaded java.nio.charset.Charset$3 from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.cs.AbstractCharsetProvider from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.cs.ext.ExtendedCharsets from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.lang.Class$1 from shared objects file]
[Loaded sun.reflect.ReflectionFactory$1 from shared objects file]
[Loaded sun.reflect.NativeConstructorAccessorImpl from shared objects file]
[Loaded sun.reflect.DelegatingConstructorAccessorImpl from shared objects file]
[Loaded java.util.SortedMap from shared objects file]
[Loaded java.util.NavigableMap from shared objects file]
[Loaded java.util.TreeMap from shared objects file]
[Loaded sun.misc.ASCIICaseInsensitiveComparator from shared objects file]
[Loaded java.util.TreeMap$Entry from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.misc.VM from shared objects file]
[Loaded java.lang.StringBuilder from shared objects file]
[Opened D:\Program Files\Java\jre7\lib\charsets.jar]
[Loaded sun.nio.cs.HistoricallyNamedCharset from shared objects file]
[Loaded sun.nio.cs.ext.GBK from D:\Program Files\Java\jre7\lib\charsets.jar]
[Loaded java.lang.StringCoding from shared objects file]
[Loaded java.lang.ThreadLocal$ThreadLocalMap from shared objects file]
[Loaded java.lang.ThreadLocal$ThreadLocalMap$Entry from shared objects file]
[Loaded java.lang.StringCoding$StringDecoder from shared objects file]
[Loaded sun.nio.cs.ext.DoubleByte from D:\Program Files\Java\jre7\lib\charsets.jar]
[Loaded sun.nio.cs.ext.DelegatableDecoder from D:\Program Files\Java\jre7\lib\charsets.jar]
[Loaded java.nio.charset.CharsetDecoder from shared objects file]
[Loaded sun.nio.cs.ext.DoubleByte$Decoder from D:\Program Files\Java\jre7\lib\charsets.jar]
[Loaded java.nio.charset.CodingErrorAction from shared objects file]
[Loaded sun.nio.cs.ArrayDecoder from shared objects file]
[Loaded java.nio.ByteBuffer from shared objects file]
[Loaded java.nio.HeapByteBuffer from shared objects file]
[Loaded java.nio.Bits from shared objects file]
[Loaded java.nio.ByteOrder from shared objects file]
[Loaded sun.misc.JavaNioAccess from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.nio.Bits$1 from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.misc.SharedSecrets from shared objects file]
[Loaded java.lang.Readable from shared objects file]
[Loaded java.nio.CharBuffer from shared objects file]
[Loaded java.nio.HeapCharBuffer from shared objects file]
[Loaded java.nio.charset.CoderResult from shared objects file]
[Loaded java.nio.charset.CoderResult$Cache from shared objects file]
[Loaded java.nio.charset.CoderResult$1 from shared objects file]
[Loaded java.nio.charset.CoderResult$2 from shared objects file]
[Loaded sun.misc.Version from shared objects file]
[Loaded sun.misc.JavaLangAccess from shared objects file]
[Loaded java.lang.System$2 from shared objects file]
[Loaded java.lang.Runtime from shared objects file]
[Loaded java.io.File from shared objects file]
[Loaded java.io.FileSystem from shared objects file]
[Loaded java.io.Win32FileSystem from shared objects file]
[Loaded java.io.WinNTFileSystem from shared objects file]
[Loaded java.io.ExpiringCache from shared objects file]
[Loaded java.util.LinkedHashMap from shared objects file]
[Loaded java.io.ExpiringCache$1 from shared objects file]
[Loaded java.util.LinkedHashMap$Entry from shared objects file]
[Loaded sun.security.action.GetPropertyAction from shared objects file]
[Loaded sun.misc.BootClassLoaderHook from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.lang.ClassLoader$3 from shared objects file]
[Loaded java.io.ExpiringCache$Entry from shared objects file]
[Loaded java.lang.ClassLoader$NativeLibrary from shared objects file]
[Loaded java.lang.StringCoding$StringEncoder from shared objects file]
[Loaded java.nio.charset.CharsetEncoder from shared objects file]
[Loaded sun.nio.cs.ext.DoubleByte$Encoder from D:\Program Files\Java\jre7\lib\charsets.jar]
[Loaded sun.nio.cs.ArrayEncoder from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.io.Closeable from shared objects file]
[Loaded java.io.InputStream from shared objects file]
[Loaded java.io.FileInputStream from shared objects file]
[Loaded java.io.FileDescriptor from shared objects file]
[Loaded sun.misc.JavaIOFileDescriptorAccess from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.io.FileDescriptor$1 from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.io.Flushable from shared objects file]
[Loaded java.io.OutputStream from shared objects file]
[Loaded java.io.FileOutputStream from shared objects file]
[Loaded java.io.FilterInputStream from shared objects file]
[Loaded java.io.BufferedInputStream from shared objects file]
[Loaded java.util.concurrent.atomic.AtomicReferenceFieldUpdater from shared objects file]
[Loaded java.util.concurrent.atomic.AtomicReferenceFieldUpdater$AtomicReferenceFieldUpdaterImpl from
 shared objects file]
[Loaded sun.reflect.misc.ReflectUtil from shared objects file]
[Loaded java.io.FilterOutputStream from shared objects file]
[Loaded java.io.PrintStream from shared objects file]
[Loaded java.io.BufferedOutputStream from shared objects file]
[Loaded java.io.Writer from shared objects file]
[Loaded java.io.OutputStreamWriter from shared objects file]
[Loaded sun.nio.cs.StreamEncoder from shared objects file]
[Loaded java.io.BufferedWriter from shared objects file]
[Loaded java.lang.Terminator from shared objects file]
[Loaded sun.misc.SignalHandler from shared objects file]
[Loaded java.lang.Terminator$1 from shared objects file]
[Loaded sun.misc.Signal from shared objects file]
[Loaded sun.misc.NativeSignalHandler from shared objects file]
[Loaded sun.misc.OSEnvironment from shared objects file]
[Loaded sun.io.Win32ErrorMode from shared objects file]
[Loaded java.lang.NullPointerException from shared objects file]
[Loaded java.lang.ArithmeticException from shared objects file]
[Loaded java.lang.Compiler from shared objects file]
[Loaded java.lang.Compiler$1 from shared objects file]
[Loaded sun.misc.Launcher from shared objects file]
[Loaded java.net.URLStreamHandlerFactory from shared objects file]
[Loaded sun.misc.Launcher$Factory from shared objects file]
[Loaded java.security.SecureClassLoader from shared objects file]
[Loaded java.net.URLClassLoader from shared objects file]
[Loaded sun.misc.Launcher$ExtClassLoader from shared objects file]
[Loaded sun.security.util.Debug from shared objects file]
[Loaded sun.misc.JavaNetAccess from shared objects file]
[Loaded java.net.URLClassLoader$7 from shared objects file]
[Loaded java.util.Enumeration from shared objects file]
[Loaded java.util.StringTokenizer from shared objects file]
[Loaded java.security.PrivilegedExceptionAction from shared objects file]
[Loaded sun.misc.Launcher$ExtClassLoader$1 from shared objects file]
[Loaded sun.misc.MetaIndex from shared objects file]
[Loaded java.io.Reader from shared objects file]
[Loaded java.io.BufferedReader from shared objects file]
[Loaded java.io.InputStreamReader from shared objects file]
[Loaded java.io.FileReader from shared objects file]
[Loaded sun.nio.cs.StreamDecoder from shared objects file]
[Loaded java.util.ArrayList from shared objects file]
[Loaded java.lang.reflect.Array from shared objects file]
[Loaded java.util.Locale from shared objects file]
[Loaded java.util.concurrent.ConcurrentMap from shared objects file]
[Loaded java.util.concurrent.ConcurrentHashMap from shared objects file]
[Loaded java.util.concurrent.locks.Lock from shared objects file]
[Loaded java.util.concurrent.locks.ReentrantLock from shared objects file]
[Loaded java.util.concurrent.ConcurrentHashMap$Segment from shared objects file]
[Loaded java.util.concurrent.locks.AbstractOwnableSynchronizer from shared objects file]
[Loaded java.util.concurrent.locks.AbstractQueuedSynchronizer from shared objects file]
[Loaded java.util.concurrent.locks.ReentrantLock$Sync from shared objects file]
[Loaded java.util.concurrent.locks.ReentrantLock$NonfairSync from shared objects file]
[Loaded java.util.concurrent.locks.AbstractQueuedSynchronizer$Node from shared objects file]
[Loaded java.util.concurrent.ConcurrentHashMap$HashEntry from shared objects file]
[Loaded java.lang.CharacterData from shared objects file]
[Loaded java.lang.CharacterDataLatin1 from shared objects file]
[Loaded java.io.ObjectStreamClass from shared objects file]
[Loaded sun.net.www.ParseUtil from shared objects file]
[Loaded java.util.BitSet from shared objects file]
[Loaded java.net.URL from shared objects file]
[Loaded java.net.Parts from shared objects file]
[Loaded java.net.URLStreamHandler from shared objects file]
[Loaded sun.net.www.protocol.file.Handler from shared objects file]
[Loaded java.security.CodeSource from shared objects file]
[Loaded java.security.Principal from shared objects file]
[Loaded java.util.HashSet from shared objects file]
[Loaded sun.misc.URLClassPath from shared objects file]
[Loaded sun.net.www.protocol.jar.Handler from shared objects file]
[Loaded sun.misc.Launcher$AppClassLoader from shared objects file]
[Loaded sun.misc.Launcher$AppClassLoader$1 from shared objects file]
[Loaded java.lang.SystemClassLoaderAction from shared objects file]
[Loaded java.lang.Enum from shared objects file]
[Loaded sun.launcher.LauncherHelper from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.net.URLClassLoader$1 from shared objects file]
[Loaded sun.net.util.URLUtil from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.misc.URLClassPath$3 from shared objects file]
[Loaded sun.misc.URLClassPath$Loader from shared objects file]
[Loaded sun.misc.URLClassPath$JarLoader from shared objects file]
[Loaded sun.nio.cs.ThreadLocalCoders from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.cs.ThreadLocalCoders$Cache from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.cs.ThreadLocalCoders$1 from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.cs.ThreadLocalCoders$2 from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.cs.Unicode from shared objects file]
[Loaded sun.nio.cs.UTF_8 from shared objects file]
[Loaded sun.nio.cs.UTF_8$Decoder from shared objects file]
[Loaded java.security.PrivilegedActionException from shared objects file]
[Loaded sun.misc.URLClassPath$FileLoader from shared objects file]
[Loaded sun.misc.Resource from shared objects file]
[Loaded sun.misc.URLClassPath$FileLoader$1 from shared objects file]
[Loaded java.lang.Package from shared objects file]
[Loaded sun.nio.ByteBuffered from shared objects file]
[Loaded sun.misc.PerfCounter from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.misc.Perf$GetPerfAction from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.misc.Perf from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.misc.PerfCounter$CoreCounters from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.nio.ch.DirectBuffer from shared objects file]
[Loaded java.nio.MappedByteBuffer from shared objects file]
[Loaded java.nio.DirectByteBuffer from shared objects file]
[Loaded java.nio.LongBuffer from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.nio.DirectLongBufferU from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded java.security.PermissionCollection from shared objects file]
[Loaded java.security.Permissions from shared objects file]
[Loaded java.net.URLConnection from shared objects file]
[Loaded sun.net.www.URLConnection from shared objects file]
[Loaded sun.net.www.protocol.file.FileURLConnection from shared objects file]
[Loaded sun.net.www.MessageHeader from shared objects file]
[Loaded java.io.FilePermission from shared objects file]
[Loaded java.io.FilePermission$1 from shared objects file]
[Loaded java.io.FilePermissionCollection from shared objects file]
[Loaded java.security.AllPermission from shared objects file]
[Loaded java.security.UnresolvedPermission from shared objects file]
[Loaded java.security.BasicPermissionCollection from shared objects file]
[Loaded jvm.base.classload.TestClassLoad from file:/D:/study/tempProject/JavaLearn/classes/]
[Loaded jvm.base.classload.A from file:/D:/study/tempProject/JavaLearn/classes/]
[Loaded sun.reflect.NativeMethodAccessorImpl from shared objects file]
[Loaded sun.reflect.DelegatingMethodAccessorImpl from shared objects file]
Hello, 0
Hello, 1
Hello, 2
Hello, 3
Hello, 4
Hello, 5
Hello, 6
Hello, 7
Hello, 8
Hello, 9
Hello, 10
Hello, 11
Hello, 12
Hello, 13
Hello, 14
[Loaded sun.reflect.ClassFileConstants from shared objects file]
[Loaded sun.reflect.AccessorGenerator from shared objects file]
[Loaded sun.reflect.MethodAccessorGenerator from shared objects file]
[Loaded sun.reflect.ByteVectorFactory from shared objects file]
[Loaded sun.reflect.ByteVector from shared objects file]
[Loaded sun.reflect.ByteVectorImpl from shared objects file]
[Loaded sun.reflect.ClassFileAssembler from shared objects file]
[Loaded sun.reflect.UTF8 from shared objects file]
[Loaded java.lang.Void from shared objects file]
[Loaded sun.reflect.Label from shared objects file]
[Loaded sun.reflect.Label$PatchInfo from shared objects file]
[Loaded java.util.ArrayList$Itr from D:\Program Files\Java\jre7\lib\rt.jar]
[Loaded sun.reflect.MethodAccessorGenerator$1 from shared objects file]
[Loaded sun.reflect.ClassDefiner from shared objects file]
[Loaded sun.reflect.ClassDefiner$1 from shared objects file]
[Loaded sun.reflect.GeneratedMethodAccessor1 from __JVM_DefineClass__]
Hello, 15
[Loaded java.lang.Shutdown from shared objects file]
[Loaded java.lang.Shutdown$Lock from shared objects file]

D:\study\tempProject\JavaLearn\classes>

*/
