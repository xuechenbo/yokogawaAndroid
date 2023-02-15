package com.yokogawa.xc.bean;

import java.util.List;

public class ExamTBean {

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", children=" + children +
                '}';
    }

    private String id;
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
            private int id;
            private String content;
            private List<Children2> children2;

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
                private int id;
                private String content;
                private Object answer;
                private Object answe;

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

                public Object getAnswer() {
                    return answer;
                }

                public void setAnswer(Object answer) {
                    this.answer = answer;
                }

                public Object getAnswe() {
                    return answe;
                }

                public void setAnswe(Object answe) {
                    this.answe = answe;
                }
            }
        }
    }
}
