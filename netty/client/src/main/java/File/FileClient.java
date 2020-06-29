package File;

import io.netty.buffer.ByteBuf;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author baopc@tuya.com
 * @date 2020/1/14
 */
public class FileClient {

    public static void main(String[] args) throws Exception{
        RandomAccessFile aFile = new RandomAccessFile("/Users/zhaidai/logs/sql.log","r");
        FileChannel fileChannel = aFile.getChannel();

        ByteBuffer bf = ByteBuffer.allocate(1024);

        int byteRead = fileChannel.read(bf);

        while (byteRead != -1){
            bf.flip();

            while (bf.remaining() > 0){
                System.out.print((char)bf.get());
            }

            bf.clear();
            byteRead = fileChannel.read(bf);
        }
        aFile.close();
    }
}
