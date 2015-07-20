package org.qq.mlnlp.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by qingqingcai on 7/17/15.
 */
public class Instance implements Serializable {

    private Object data;
    private Object target;
    private Object name;
    private Object source;

    boolean locked = false;

    public Instance(Object data, Object target, Object name, Object source) {
        this.data = data;
        this.target = target;
        this.name = name;
        this.source = source;
    }

    public Object getData() {
        return data;
    }

    public Object getTarget() {
        return target;
    }

    public Object getName() {
        return name;
    }

    public Object getSource() {
        return source;
    }

    public void setData(Object d) {
        if (!locked)
            data = d;
        else
            throw new IllegalStateException("Instance is locked!");
    }

    public void setTarget(Object t) {
        if (!locked)
            target = t;
        else
            throw new IllegalStateException("Instance is locked!");
    }

    public void setName(Object n) {
        if (!locked)
            name = n;
        else
            throw new IllegalStateException("Instance is locked!");
    }

    public void setSource(Object s) {
        if (!locked)
            source = s;
        else
            throw new IllegalStateException("Instance is locked!");
    }


    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    // Serialization of Instance
    private static final long serialVersionUID = 1;
    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(CURRENT_SERIAL_VERSION);
            out.writeObject(data);
            out.writeObject(target);
            out.writeObject(name);
            out.writeObject(source);
            out.writeObject(locked);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream in) {
        try {
            int version = in.readInt();
            data = in.readObject();
            target = in.readObject();
            name = in.readObject();
            source = in.readObject();
            locked = in.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
