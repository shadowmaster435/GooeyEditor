package org.shadowmaster435.gooeyeditor.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ClassCodeStringBuilder {

    private final String className;
    private final ArrayList<MethodStringBuilder> methods = new ArrayList<>();
    private final ArrayList<FieldStringBuilder> fields = new ArrayList<>();
    private final ArrayList<Class<?>> classesToImport = new ArrayList<>();
    private Class<?> extending = null;
    private Class<?>[] implementing = null;

    public ClassCodeStringBuilder(String className) {
        this.className = className;
    }

    public ClassCodeStringBuilder(String className, Class<?> extending) {
        this.className = className;
        this.extending = extending;
    }
    public ClassCodeStringBuilder(String className, Class<?>... implementing) {
        this.className = className;
        if (implementing.length > 0) {
            this.implementing = implementing;
        }
    }

    public ClassCodeStringBuilder(String className, Class<?> extending, Class<?>... implementing) {
        this.className = className;
        this.extending = extending;
        if (implementing.length > 0) {
            this.implementing = implementing;
        }
    }

    public String build() {
        var header = buildClassHeader();
        var fields = buildFields();
        var methods = buildMethods();
        var imports = buildImports();
        classesToImport.clear();

        return String.format("%1$s%2$s {\n%3$s\n}", imports, header, fields + "\n" + methods);
    }

    public ClassCodeStringBuilder method(MethodStringBuilder methodStringBuilder) {
        methods.add(methodStringBuilder);
        return this;
    }

    public ClassCodeStringBuilder field(FieldStringBuilder fieldStringBuilder) {
        fields.add(fieldStringBuilder);
        return this;
    }

    //region String Builders
    public static class NewInstanceStringBuilder {
        private StringBuilder builder = new StringBuilder();
        private Class<?> type;
        private ArrayList<Class<?>> classesToImport = new ArrayList<>();

        public NewInstanceStringBuilder(Class<?> type) {
            this.type = type;
            classesToImport.add(type);
        }

        public NewInstanceStringBuilder add(Object value) {
            builder.append(value.toString()).append(", ");
            return this;
        }

        public NewInstanceStringBuilder add(String varName) {
            builder.append(varName).append(", ");
            return this;
        }

        public String build() {
            var str = builder.toString();

            return String.format("new %1$s(%2$s)", getSimpleCanonicalName(type), builder.substring(0, str.length() - 2));
        }

        public Class<?> getType() {
            return type;
        }

        public ArrayList<Class<?>> getClassesToImport() {
            return classesToImport;
        }
    }
    
    private static String getSimpleCanonicalName(Class<?> clazz) {
        var cannon = clazz.getCanonicalName();
        return cannon.substring(cannon.lastIndexOf(".") + 1);
    }
    
    public static class TupleStringBuilder {
        private final StringBuilder builder = new StringBuilder().append("(");
        private final ArrayList<Class<?>> classesToImport = new ArrayList<>();

        public TupleStringBuilder add(Class<?> type, String name) {
            if (!classesToImport.contains(type)) {
                classesToImport.add(type);
            }
            builder.append(getSimpleCanonicalName(type)).append(" ").append(name).append(", ");
            return this;
        }

        public String build() {
            var str = builder.toString();
            return str.substring(0, str.length() - 2) + ")";
        }

        public ArrayList<Class<?>> getClassesToImport() {
            return classesToImport;
        }
    }

    public static class FieldStringBuilder {
        private final ArrayList<Class<?>> classesToImport = new ArrayList<>();
        private final Class<?> type;
        private final String fieldName;

        public FieldStringBuilder(Class<?> type, String fieldName) {
            this.type = type;
            this.fieldName = fieldName;
            classesToImport.add(type);
        }

        public String build() {
            return getSimpleCanonicalName(type) + " " + fieldName + ";";
        }

        public String build(NewInstanceStringBuilder newInstanceStringBuilder) {
            if (!classesToImport.contains(newInstanceStringBuilder.getType())) {
                classesToImport.add(newInstanceStringBuilder.getType());
            }
            var built = newInstanceStringBuilder.build();
            classesToImport.clear();
            return getSimpleCanonicalName(type) + " " + fieldName + " = " + built + ";";
        }

        public ArrayList<Class<?>> getClassesToImport() {
            return classesToImport;
        }
    }

    public static class MethodStringBuilder {
        private final StringBuilder builder = new StringBuilder();
        private final ArrayList<Class<?>> classesToImport = new ArrayList<>();

        public MethodStringBuilder(String methodName, @Nullable Class<?> type, @Nullable TupleStringBuilder tupleStringBuilder) {
            builder
                    .append("\tpublic ")
                    .append((type != null) ? getSimpleCanonicalName(type) : "void ")
                    .append(methodName)
                    .append((tupleStringBuilder != null) ? "(" + tupleStringBuilder.build() + ") " + " " : "() ")
                    .append("{\n");
            if (tupleStringBuilder != null) {
                classesToImport.addAll(tupleStringBuilder.getClassesToImport());
            }
            if (type != null) {
                classesToImport.add(type);
            }
        }

        public MethodStringBuilder field(FieldStringBuilder field) {
            var built = field.build();
            builder.append("\t\t").append(built).append("\n");
            classesToImport.addAll(field.classesToImport);
            field.classesToImport.clear();
            return this;
        }

        public MethodStringBuilder line(String text) {
            builder.append("\t\t").append(text).append("\n");
            return this;
        }

        public MethodStringBuilder assign(String assignee, String assigner) {
            builder.append("\t\t").append(assignee).append(" = ").append(assigner).append(";");
            return this;
        }

        public MethodStringBuilder assign(String assignee, Number assigner) {
            builder.append("\t\t").append(assignee).append(" = ").append(assigner).append(";");
            return this;
        }

        public MethodStringBuilder assign(String assignee, NewInstanceStringBuilder assigner) {
            var built = assigner.build();
            classesToImport.addAll(assigner.getClassesToImport());
            builder.append("\t\t").append(assignee).append(" = ").append(built).append(";\n");
            assigner.getClassesToImport().clear();

            return this;
        }

        public ArrayList<Class<?>> getClassesToImport() {
            return classesToImport;
        }

        public String build() {
            var built = builder.append("\n\t}").toString();
            classesToImport.clear();
            return built;
        }
        public String build(String returns) {
            return builder.append("\t\treturn ").append(returns).append(";\n\t}").toString();
        }
    }

    private String buildMethods() {
        StringBuilder builder = new StringBuilder();

        for (MethodStringBuilder methodStringBuilder : methods) {
            builder.append(methodStringBuilder.build()).append("\n");
            classesToImport.addAll(methodStringBuilder.getClassesToImport());
        }
        return builder.toString();
    }

    private String buildFields() {
        StringBuilder builder = new StringBuilder();

        for (FieldStringBuilder fieldStringBuilder : fields) {
            builder.append("\tpublic ").append(fieldStringBuilder.build()).append("\n");
            classesToImport.addAll(fieldStringBuilder.getClassesToImport());
        }
        return builder.toString();
    }

    private String buildImports() {
        StringBuilder builder = new StringBuilder();
        ArrayList<Class<?>> alreadyImported = new ArrayList<>();
        for (Class<?> clazz : classesToImport) {
            if (clazz.getPackage().getName().equals("java.lang")) {
                continue;
            }
            if (!alreadyImported.contains(clazz)) {
                builder.append(String.format("import %s;\n", clazz.getCanonicalName()));
                alreadyImported.add(clazz);
            }
        }
        return builder.toString();
    }

    private String buildClassHeader() {
        StringBuilder builder = new StringBuilder().append("\npublic class ").append(className);
        if (extending != null) {
            if (!classesToImport.contains(extending)) {
                classesToImport.add(extending);
            }
            builder.append(" extends ").append(getSimpleCanonicalName(extending));
        }
        if (implementing != null) {
            var lastEntry = implementing[implementing.length - 1];
            for (Class<?> clazz : implementing) {
                if (!classesToImport.contains(clazz)) {
                    classesToImport.add(clazz);
                }
                builder.append(getSimpleCanonicalName(clazz)).append((clazz != lastEntry) ? "," : "");
            }
        }
        return builder.toString();
    }
    //endregion
}
