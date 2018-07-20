package com.stone.templateapp.demo.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午8:00.
 */
public class BinderPoolCopy {

    private static final String TAG = "BinderPool";
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private static volatile BinderPoolCopy sInstance;
    private Context ctx;

    private BinderPoolCopy(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPoolCopy getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPoolCopy.class) {
                if (sInstance == null) {
                    sInstance = new BinderPoolCopy(context);
                }
            }
        }
        return sInstance;
    }

    private void connectBinderPoolService() {

    }

    private IBinderPool mBinderPool;
    private IBinder.DeathRecipient mDeath = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mDeath, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mDeath, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {

            IBinder binder = null;
            switch (binderCode) {
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }
}
