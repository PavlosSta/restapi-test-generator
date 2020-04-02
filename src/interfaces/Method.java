package interfaces;

public interface Method {

    String getType();

    void setType(String type);

    RequestSpec getReq();

    void setReq(RequestSpec req);

    ResponseSpec getRes();

    void setRes(ResponseSpec ses);

}

