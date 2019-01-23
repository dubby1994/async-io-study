package cn.dubby.async.io.core.constant;

/**
 * @author dubby
 * @date 2019/1/22 20:07
 */
public class ReadType {

    public static final ReadType Init = new ReadType();
    /**
     * 2bytes
     */
    public static final ReadType MagicNumber = new ReadType();
    /**
     * 2bytes
     */
    public static final ReadType Version = new ReadType();
    /**
     * 4bytes
     */
    public static final ReadType Length = new ReadType();
    /**
     * 8bytes
     */
    public static final ReadType UniqueId = new ReadType();

    public static final ReadType Data = new ReadType();

    static {
        Init.next = MagicNumber;
        Init.offset = 0;

        MagicNumber.next = Version;
        MagicNumber.offset = 2;

        Version.next = Length;
        Version.offset = 4;

        Length.next = UniqueId;
        Length.offset = 8;

        UniqueId.next = Data;
        UniqueId.offset = 16;

        Data.next = Init;
        Data.offset = 0;
    }


    private ReadType next;

    private int offset;

    public ReadType getNext() {
        return next;
    }

    public int getOffset() {
        return offset;
    }
}
