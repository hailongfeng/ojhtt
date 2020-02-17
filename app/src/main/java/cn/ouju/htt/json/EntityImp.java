package cn.ouju.htt.json;


import java.io.Serializable;

/**
 *
 */
public interface EntityImp extends Serializable {
    <T extends EntityImp> T newObject();

    void paseFromJson(JsonUtils jsonUtils);
}
