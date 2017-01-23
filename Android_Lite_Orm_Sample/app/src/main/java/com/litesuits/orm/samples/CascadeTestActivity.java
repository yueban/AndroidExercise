package com.litesuits.orm.samples;

import android.os.Bundle;
import com.litesuits.android.log.Log;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.R;
import com.litesuits.orm.db.DataBase;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.litesuits.orm.model.cascade.tomany.Student;
import com.litesuits.orm.model.cascade.tomany.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CascadeTestActivity extends BaseActivity {
    static Teacher teacher1;
    static Teacher teacher2;

    static Student studentA;
    static Student studentB;
    static Student studentC;


    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSubTitile(getString(R.string.sub_title));
        mockData();
        db = LiteOrm.newCascadeInstance(this, "cascade.db");
    }

    private void makeOrmTest(int id) {
        switch (id) {
            case 0:
                testSave();
                break;
            case 1:
                testInsert();
                break;
            case 2:
                testUpdate();
                break;
            case 3:
                testUpdateColumn();
                break;
            case 4:
                testQueryAll();
                break;
            case 5:
                testQueryByWhere();
                break;
            case 6:
                testQueryByID();
                break;
            case 7:
                testQueryAnyUwant();
                break;
            case 8:
                testMapping();
                break;
            case 9:
                testDelete();
                break;
            case 10:
                testDeleteByIndex();
                break;
            case 11:
                testDeleteByWhereBuilder();
                break;
            case 12:
                testDeleteAll();
                break;
            default:
                break;
        }
    }

    private void testSave() {
        db.save(teacher0);
    }

    private void testInsert() {
        ArrayList<Teacher> ts = new ArrayList<Teacher>();
        ts.add(teacher1);
        ts.add(teacher2);
        db.insert(ts, ConflictAlgorithm.Fail);
    }

    private void testUpdate() {

    }

    private void testUpdateColumn() {

    }

    private void testQueryAll() {
        queryAndPrintAll(Teacher.class);
        queryAndPrintAll(Student.class);
    }

    private void testQueryByWhere() {

    }

    private void testQueryByID() {

    }

    private void testQueryAnyUwant() {

    }

    private void testMapping() {

    }

    private void testDelete() {

    }

    private void testDeleteByIndex() {

    }

    private void testDeleteByWhereBuilder() {

    }

    private void testDeleteAll() {
        db.deleteAll(Teacher.class);
        db.deleteAll(Student.class);
    }


    private void queryAndPrintAll(Class claxx) {
        List list = db.queryAll(claxx);
        Log.i(TAG, list);
    }

    static Teacher teacher0;
    static Student studentD;

    private void mockData() {
        if (teacher1 != null) {
            return;
        }

        /**********************************************
         简单的双向关联
         T0 <-> SA
         */
        teacher0 = new Teacher("T0");
        studentA = new Student("S0");

        teacher0.setStudentLinkedQueue(new ConcurrentLinkedQueue<Student>());
        teacher0.getStudentLinkedQueue().add(studentA);

        studentA.setTeachersArray(new Teacher[1]);
        studentA.getTeachersArray()[0] = teacher0;


        /**********************************************
         较多的双向关联
         T1 <-> SB\SC
         T2 <-> SC\SD
         */
        teacher1 = new Teacher("T1");
        teacher2 = new Teacher("T2");

        studentB = new Student("SB");
        studentC = new Student("SC");
        studentD = new Student("SD");

        teacher1.setStudentLinkedQueue(new ConcurrentLinkedQueue<Student>());
        teacher1.getStudentLinkedQueue().add(studentB);
        teacher1.getStudentLinkedQueue().add(studentC);

        teacher2.setStudentLinkedQueue(new ConcurrentLinkedQueue<Student>());
        teacher2.getStudentLinkedQueue().add(studentC);
        teacher2.getStudentLinkedQueue().add(studentD);

        studentB.setTeachersArray(new Teacher[1]);
        studentB.getTeachersArray()[0] = teacher1;

        studentC.setTeachersArray(new Teacher[2]);
        studentC.getTeachersArray()[0] = teacher1;
        studentC.getTeachersArray()[0] = teacher2;

        studentD.setTeachersArray(new Teacher[1]);
        studentD.getTeachersArray()[0] = teacher2;


    }


    @Override
    public String getMainTitle() {
        return getString(R.string.title_cascade);
    }

    @Override
    public String[] getButtonTexts() {
        return getResources().getStringArray(R.array.orm_test_list);
    }

    @Override
    public Runnable getButtonClickRunnable(final int id) {
        return new Runnable() {
            @Override
            public void run() {
                //Sub Thread
                makeOrmTest(id);
            }
        };
    }
}
