package io.left.hellomesh;

import io.left.rightmesh.id.MeshID;

/**
 * Created by sallyp on 2018-01-14.
 */

public class Peer {

    private MeshID meshID;
    private String name;

    public Peer(MeshID meshID, String name) {
        this.meshID = meshID;
        this.name = name;
    }

    public MeshID getMeshID() {
        return meshID;
    }

    public void setMeshID(MeshID meshID) {
        this.meshID = meshID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
