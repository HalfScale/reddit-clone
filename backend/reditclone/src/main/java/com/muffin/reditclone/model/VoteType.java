package com.muffin.reditclone.model;

import com.muffin.reditclone.exception.SpringRedditException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;

    VoteType(int direction) {
    }

   
}
