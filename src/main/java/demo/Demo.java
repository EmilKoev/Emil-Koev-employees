package demo;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static demo.Reader.*;

public class Demo {

    public static void main(String[] args) {

        try {
            HashMap<String, ArrayList<Employee>> projects = new HashMap<>();
            //also works with jason files
            File file = chooseFile();
            System.out.println(file.getName());
            if (file.getName().endsWith(".txt")){
                projects = txtFormat(file);
            }
            if (file.getName().endsWith(".json")){
                projects = jsonFormat(file);
                for (ArrayList<Employee> emp : projects.values()) {
                    for (Employee e : emp) {
                        if (e.getEndAt() == null){
                            e.setEndAt(LocalDate.now());
                        }
                    }
                }
            }

            TreeSet<TeamWork> teamWorks = new TreeSet<>((o1,o2) -> (comparePeriod(o2.getWorkTogether(),o1.getWorkTogether())));
            for (Map.Entry<String, ArrayList<Employee>> entry : projects.entrySet()) {
                ArrayList<Employee> list = entry.getValue();
                for (int i = 0; i < list.size()-1; i++) {
                    for (int j = i+1; j < list.size(); j++) {
                        Employee e1 = list.get(i);
                        Employee e2 = list.get(j);
                        if (e1.equals(e2)){
                            continue;
                        }
                        Period p;
                        if (e1.getEndAt().isBefore(e2.getStartAt()) || e2.getEndAt().isBefore(e1.getStartAt())){
                            continue;
                        }
                        if (e1.getStartAt().isBefore(e2.getStartAt())){
                            if (e2.getEndAt().isBefore(e1.getEndAt())){
                                p = Period.between(e2.getStartAt(),e2.getEndAt());
                            }else {
                                p = Period.between(e2.getStartAt(),e1.getEndAt());
                            }
                        }else {
                            if (e1.getEndAt().isBefore(e2.getEndAt())){
                                p = Period.between(e1.getStartAt(),e1.getEndAt());
                            }else {
                                p = Period.between(e1.getStartAt(),e2.getEndAt());
                            }
                        }
                        if (teamWorks.size() == 0){
                            TeamWork teamWork = new TeamWork(e1,e2,p);
                            teamWork.addProjet(entry.getKey());
                            teamWorks.add(teamWork);
                        }else {
                            boolean hasMatch = false;
                            for (TeamWork t :teamWorks) {
                                if (t.isTheSameTeam(e1,e2)){
                                    hasMatch = true;
                                    t.addPeriod(p);
                                    t.addProjet(entry.getKey());
                                    break;
                                }
                            }
                            if (!hasMatch){
                                TeamWork t = new TeamWork(e1,e2,p);
                                teamWorks.add(t);
                                t.addProjet(entry.getKey());
                            }
                        }
                    }
                }
            }

            System.out.println(teamWorks.first());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int comparePeriod(Period period, Period period1) {
        if (period.getYears() == period1.getYears()){
            if (period.getMonths() == period1.getMonths()){
                return period.getDays()-period1.getDays();
            }else {
                return period.getMonths()-period1.getMonths();
            }
        }else {
            return period.getYears()-period1.getYears();
        }
    }
}