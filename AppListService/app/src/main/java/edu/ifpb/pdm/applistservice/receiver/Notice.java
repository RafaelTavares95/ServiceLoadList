package edu.ifpb.pdm.applistservice.receiver;

import java.util.List;

/**
 * Created by Rafael on 18/03/2016.
 */
public interface Notice {
    void process(List<String> list);
}
