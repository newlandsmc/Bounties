package com.semivanilla.bounties.utils;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.utils.utility.FileUtility;
import com.semivanilla.bounties.utils.utility.MiniMessageUtils;
import com.semivanilla.bounties.utils.utility.RankUtility;

public final class UtilityManager {

    private final Bounties javaPlugin;
    private final FileUtility fileUtility;
    private final MiniMessageUtils messageUtils;
    private final RankUtility rankUtility;

    public UtilityManager(Bounties javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.fileUtility = new FileUtility(this);
        this.messageUtils = new MiniMessageUtils(this);
        this.rankUtility = new RankUtility(this);
    }

    public Bounties getJavaPlugin() {
        return javaPlugin;
    }

    public FileUtility getFileUtility() {
        return fileUtility;
    }

    public MiniMessageUtils getMessageUtils() {
        return messageUtils;
    }

    public RankUtility getRankUtility() {
        return rankUtility;
    }
}
