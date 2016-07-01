/**
 *  Created by Furkan Mumcu on 15.06.2016
 *  A Basic example to show how to use dmn tables with camunda's dmn classes.
 *
*/

/**
 Copyright [2016] [Furkan Mumcu]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package com.example;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableImpl;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableInputImpl;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableOutputImpl;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableRuleImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static java.lang.System.out;

@SpringBootApplication
public class BasicDmnApplication {

	public static void main(String[] args) throws Exception  {
		SpringApplication.run(BasicDmnApplication.class, args);

        out.print("lel");
        int inputcount = 0;
        int outputcount = 0;

        // configure and build the DMN engine
        DmnEngine dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();

        File dmnFile = new File("src/main/resources/yemekhane.dmn");
        InputStream dmnStream = new FileInputStream(dmnFile);


        DmnDecision decision = dmnEngine.parseDecision("yemekhane", dmnStream);
        DmnDecisionTableImpl decisionTable = null;

        if(decision instanceof DmnDecisionTableImpl){
            decisionTable = (DmnDecisionTableImpl) decision;
            out.println(decisionTable.toString());
        }else{
            out.println("Decision Table cinsinde değil..");
        }

        out.println("Inputs are:");
        for (DmnDecisionTableInputImpl input : decisionTable.getInputs()) {
            inputcount++;
            out.println(input.toString());
        }

        out.println("Outputs are:");
        for (DmnDecisionTableOutputImpl output : decisionTable.getOutputs()) {
            outputcount++;
            out.println(output.toString());
        }

        out.println("Rules are:");
        for (DmnDecisionTableRuleImpl rules : decisionTable.getRules()) {
            out.println(rules.toString());
        }

        Map<String, Object> data = new HashMap<String, Object>();

        data.put("input1", "Cuma"); // entering inputs manually
        data.put("input2", "Ogle"); // entering inputs manually

        // evaluate a decision
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, data);
        out.println(result.toString());

        /** More automatical approach
         *  //////////////////////
         *  //////////////////////
         *  //////////////////////
         *  //////////////////////
         * */

        Map<String, Object> data2 = new HashMap<String, Object>();
        Scanner in = new Scanner(System.in);
        int i = 1;
        for (DmnDecisionTableInputImpl input : decisionTable.getInputs()){
            System.out.println("Enter value for " + input.name );
            Object o = new Object();
            o =  in.nextLine();
            //System.out.println("type " + o.getClass().getTypeName() );
            data2.put("input"+i, o);
            i++;
        }
        DmnDecisionTableResult result2 = dmnEngine.evaluateDecisionTable(decision, data2);
        out.println(result2.toString());


        List<String> outs = new ArrayList<>();
        for (DmnDecisionTableOutputImpl output : decisionTable.getOutputs()){
            outs.add(output.getName());
        }

        int k =0;
        Map<String, Object> treeMap = new TreeMap<String, Object>(result2.get(0).getEntryMap()); // this is for right order, because result.get(0).getEntryMap() does not give sorted result
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {

            System.out.println(outs.get(k) + ": " + entry.getValue());
            k++;
        }
    }
}