package com.dakuo.demomodule;

import cn.ctcraft.ctonlinereward.common.module.Module;
import cn.ctcraft.ctonlinereward.common.module.ModuleDescription;

import java.util.ArrayList;
import java.util.List;

public class Main extends Module {


    @Override
    public void onEnable() {
        Hello hello = new Hello();

        getLogging().info(hello.world());
    }


    @Override
    public ModuleDescription getDescription() {
        return new ModuleDescription("demo","大阔","1.0.0");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {

    }
}