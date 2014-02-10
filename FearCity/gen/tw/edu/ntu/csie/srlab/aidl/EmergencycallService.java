/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\USER\\workspace\\FearCity\\src\\tw\\edu\\ntu\\csie\\srlab\\aidl\\EmergencycallService.aidl
 */
package tw.edu.ntu.csie.srlab.aidl;
public interface EmergencycallService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements tw.edu.ntu.csie.srlab.aidl.EmergencycallService
{
private static final java.lang.String DESCRIPTOR = "tw.edu.ntu.csie.srlab.aidl.EmergencycallService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an tw.edu.ntu.csie.srlab.aidl.EmergencycallService interface,
 * generating a proxy if needed.
 */
public static tw.edu.ntu.csie.srlab.aidl.EmergencycallService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof tw.edu.ntu.csie.srlab.aidl.EmergencycallService))) {
return ((tw.edu.ntu.csie.srlab.aidl.EmergencycallService)iin);
}
return new tw.edu.ntu.csie.srlab.aidl.EmergencycallService.Stub.Proxy(obj);
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
case TRANSACTION_SendEmergencycall:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.SendEmergencycall(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements tw.edu.ntu.csie.srlab.aidl.EmergencycallService
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
public void SendEmergencycall(java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_SendEmergencycall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_SendEmergencycall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void SendEmergencycall(java.lang.String msg) throws android.os.RemoteException;
}
