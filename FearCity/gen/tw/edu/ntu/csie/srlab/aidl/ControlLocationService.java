/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\USER\\workspace\\FearCity\\src\\tw\\edu\\ntu\\csie\\srlab\\aidl\\ControlLocationService.aidl
 */
package tw.edu.ntu.csie.srlab.aidl;
public interface ControlLocationService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements tw.edu.ntu.csie.srlab.aidl.ControlLocationService
{
private static final java.lang.String DESCRIPTOR = "tw.edu.ntu.csie.srlab.aidl.ControlLocationService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an tw.edu.ntu.csie.srlab.aidl.ControlLocationService interface,
 * generating a proxy if needed.
 */
public static tw.edu.ntu.csie.srlab.aidl.ControlLocationService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof tw.edu.ntu.csie.srlab.aidl.ControlLocationService))) {
return ((tw.edu.ntu.csie.srlab.aidl.ControlLocationService)iin);
}
return new tw.edu.ntu.csie.srlab.aidl.ControlLocationService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_SetSendLocationtoActivity:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.SetSendLocationtoActivity(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements tw.edu.ntu.csie.srlab.aidl.ControlLocationService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void SetSendLocationtoActivity(boolean isSend) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isSend)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_SetSendLocationtoActivity, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_SetSendLocationtoActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void SetSendLocationtoActivity(boolean isSend) throws android.os.RemoteException;
}
