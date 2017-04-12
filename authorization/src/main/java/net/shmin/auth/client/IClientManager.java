package net.shmin.auth.client;

/**
 * Created by benjamin on 9/15/14.
 */
public interface IClientManager<T> {
    /**
     * 注册一个Client
     *
     * @param client
     */
    void registClient(T client);

    /**
     * 检查clientId的正确性
     *
     * @param client
     * @return
     */
    boolean checkClientId(T client);

    /**
     * 收回clientId
     *
     * @param client
     */
    void revokeClientId(T client);
}
