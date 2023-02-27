package com.rgr.data_structure;

import com.rgr.comparator.Comparator;
import com.rgr.type.UserType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

public class BinaryTree implements Serializable {

    private final Comparator comparator;
    Node root;

    public BinaryTree(Comparator comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    private class Node {
        private Object value;
        private Node left;
        private Node right;
        private int height;

        public Node(Object value) {
            this.value = value;
            height = 1;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    //Рекурсивная вставка
    public void insert(Object data) {
        root = insert(root, data);
    }

    private Node insert(Node node, Object data) {
        if (node == null) {
            return new Node(data);
        }
        if (comparator.compare(data, node.value) < 0) {
            node.left = insert(node.left, data);
        } else if (comparator.compare(data, node.value) > 0) {
            node.right = insert(node.right, data);
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    //Вставка в сбалансированное дерево
    private void insertBalance(Object data) {
        root = insertBalance(root, data);
    }

    private Node insertBalance(Node node, Object data) {
        if (node == null) {
            return new Node(data);
        }
        if (comparator.compare(data, node.value) < 0) {
            node.left = insertBalance(node.left, data);
        } else if (comparator.compare(data, node.value) > 0) {
            node.right = insertBalance(node.right, data);
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        int balance = getBalance(node);
        if (balance > 1 && comparator.compare(data, node.left.value) < 0) {
            return rotateRight(node);
        }
        if (balance < -1 && comparator.compare(data, node.right.value) > 0) {
            return rotateLeft(node);
        }
        if (balance > 1 && comparator.compare(data, node.left.value) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && comparator.compare(data, node.right.value) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    //Удаление по индексу
    public void removeByIndex(int index) {
        Vector<Node> nodes = new Vector<>();
        forEachInOrder(value -> {
            nodes.add(findNodeByValue(root, value));
        });
        Node nodeToRemove = nodes.get(index);
        remove(nodeToRemove.value);
    }

    private Node findNodeByValue(Node node, Object value) {
        if (node == null) {
            return null;
        }
        if (comparator.compare(value, node.value) == 0) {
            return node;
        } else if (comparator.compare(value, node.value) < 0) {
            return findNodeByValue(node.left, value);
        } else {
            return findNodeByValue(node.right, value);
        }
    }

    public void remove(Object value) {
        root = remove(root, value);
    }

    private Node remove(Node node, Object value) {
        if (node == null) {
            return null;
        }
        if (comparator.compare(value, node.value) < 0) {
            node.left = remove(node.left, value);
            return node;
        } else if (comparator.compare(value, node.value) > 0) {
            node.right = remove(node.right, value);
            return node;
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node temp = node;
                node = getMin(temp.right);
                node.right = removeMin(temp.right);
                node.left = temp.left;
                return node;
            }
        }
    }

    private Node getMin(Node node) {
        if (node.left == null) {
            return node;
        } else {
            return getMin(node.left);
        }
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        } else {
            node.left = removeMin(node.left);
            return node;
        }
    }

    //Поиск по индексу
    public Object findNodeByIndex(int index) {
        Vector<Node> nodes = new Vector<>();
        forEachInOrder(value -> {
            nodes.add(findNodeByValue(root, value));
        });
        return nodes.get(index).value;
    }

    //Балансировка AVL
    public BinaryTree balance() {
        Vector values = getNodesVector();
        BinaryTree balancedTree = new BinaryTree(this.comparator);
        for (Object value : values) {
            balancedTree.insertBalance(value);
        }
        return balancedTree;
    }

    public Vector getNodesVector() {
        Vector<Object> nodes = new Vector<>();
        forEachInOrder(value -> {
            nodes.add(findNodeByValue(root, value).value);
        });
        return nodes;
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private Node rotateLeft(Node node) {
        Node right = node.right;
        Node rightLeft = right.left;
        right.left = node;
        node.right = rightLeft;
        return right;
    }

    private Node rotateRight(Node node) {
        Node left = node.left;
        Node leftRight = left.right;
        left.right = node;
        node.left = leftRight;
        return left;
    }

    //Печать деерва
    public void printTree() {
        scan(root, 0);
    }

    private void scan(Node node, int level) {
        if (node == null) {
            return;
        }
        scan(node.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(node.value);
        scan(node.left, level + 1);
    }

    public String scan(Node node, int level, String str) {
        if (node == null) {
            return str;
        }
        String helpLeft = new String();
        helpLeft = scan(node.right, level + 1, helpLeft);
        for (int i = 0; i < level; i++) {
            helpLeft += "           ";
        }
        helpLeft += (node.value.toString() + "\n");
        String helpRight = new String();
        helpRight = scan(node.left, level + 1, helpRight);
        str = helpLeft + helpRight;
        return str;
    }

    //Итератор поперечный
    public void forEachInOrder(DoWith iterator) {
        traverseInOrder(root, iterator);
    }

    private void traverseInOrder(Node node, DoWith iterator) {
        if (node == null) {
            return;
        }
        traverseInOrder(node.left, iterator);
        iterator.doWith(node.value);
        traverseInOrder(node.right, iterator);
    }

    //Итератор прямой
    public void forEachPreOrder(DoWith iterator) {
        traversePreOrder(root, iterator);
    }

    private void traversePreOrder(Node node, DoWith iterator) {
        if (node == null) {
            return;
        }
        iterator.doWith(node.value);
        traversePreOrder(node.left, iterator);
        traversePreOrder(node.right, iterator);
    }

    //Очистка дерева
    public void clearTree() {
        this.root = null;
    }

//    //Сериализация
//    public void saveTree(UserType userType, String fileName) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
//            writer.write(userType.typeName() + "\n");
//            this.forEachPreOrder(el -> {
//                try {
//                    writer.write(el.toString() + "\n");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //Десериализация
//    public BinaryTree loadTree(UserType userType, String fileName) {
//        BinaryTree binaryTree = new BinaryTree(this.comparator);
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            line = br.readLine();
//            if (!userType.typeName().equals(line)) {
//                throw new Exception("Ошибка в структуре файла!");
//            }
//
//            while ((line = br.readLine()) != null) {
//                binaryTree.insert(userType.parseValue(line));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return binaryTree;
//    }

    @Override
    public String toString() {
        String str = "";
        return scan(root, 0, str);
    }
}
