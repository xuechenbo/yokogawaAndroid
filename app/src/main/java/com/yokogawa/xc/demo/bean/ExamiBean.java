package com.yokogawa.xc.demo.bean;

import android.content.Context;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.yokogawa.xc.room.converter.AAAConverters;
import com.yokogawa.xc.room.converter.ExamCoverters;

import java.util.List;

@Entity
@TypeConverters({ExamCoverters.class})
public class ExamiBean {
    @PrimaryKey(autoGenerate = true)
    public int mid;
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "children")
    private List<Children> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public static class Children {

        public boolean isFinished(Context context) {
            boolean isFinish = true;
            List<Children1> children1 = getChildren1();
            for (int i = 0; i < children1.size(); i++) {
                isFinish = children1.get(i).isFinish();
                if (!isFinish) {
                    isFinish = false;
                    break;
                }
            }
            return isFinish;
        }

        @Override
        public String toString() {
            return "{" +
                    "id='" + id + '\'' +
                    ", children1=" + children1 +
                    '}';
        }

        private String id;
        private List<Children1> children1;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Children1> getChildren1() {
            return children1;
        }

        public void setChildren1(List<Children1> children1) {
            this.children1 = children1;
        }

        public static class Children1 {

            public boolean isFinish() {
                boolean isFinish = true;
                List<Children2> children2 = getChildren2();
                for (int i = 0; i < children2.size(); i++) {
                    isFinish = children2.get(i).isFinish();
                    if (!isFinish) {
                        isFinish = false;
                        break;
                    }
                }
                return isFinish;
            }

            @Override
            public String toString() {
                return "{" +
                        "id=" + id +
                        ", content='" + content + '\'' +
                        ", answer='" + answer + '\'' +
                        ", children2=" + children2 +
                        '}';
            }

            private int id;
            private String content;
            private String answer;
            private List<Children2> children2;

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<Children2> getChildren2() {
                return children2;
            }

            public void setChildren2(List<Children2> children2) {
                this.children2 = children2;
            }

            public static class Children2 {

                public boolean isFinish() {
                    boolean isFinish = true;
                    if (getAnswer() == null) {
                        return false;
                    } else if (getAnswer().equals("null")) {
                    } else {
                        String[] split = getAnswer().split(",");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].trim().isEmpty()) {
                                isFinish = false;
                                break;
                            }
                        }
                    }
                    return isFinish;
                }

                @Override
                public String toString() {
                    return "{" +
                            "id=" + id +
                            ", content='" + content + '\'' +
                            ", answer='" + answer + '\'' +
                            '}';
                }

                private int id;
                private String content;
                private String answer;

                public String getAnswer() {
                    return answer;
                }

                public void setAnswer(String answer) {
                    this.answer = answer;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", children=" + children +
                '}';
    }

    public boolean isFinished(Context context) {
        boolean isType = true;
        for (int i = 0; i < getChildren().size(); i++) {
            isType = getChildren().get(i).isFinished(context);

            if (!isType) {
                isType = false;
                break;
            }
        }
        return isType;
    }

}
