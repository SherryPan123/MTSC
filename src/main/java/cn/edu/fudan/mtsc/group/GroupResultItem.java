package cn.edu.fudan.mtsc.group;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sherry on 17-3-27.
 */
public class GroupResultItem implements Serializable {

    private int groupId;
    private List<Integer> ids;

    public GroupResultItem(int groupId, List<Integer> ids) {
        this.groupId = groupId;
        this.ids = ids;
    }

    public int getGroupId() {
        return groupId;
    }

    public List<Integer> getIds() {
        return ids;
    }

}

