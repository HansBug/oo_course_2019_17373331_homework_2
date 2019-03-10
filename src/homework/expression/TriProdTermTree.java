package homework.expression;

import homework.expression.core.Computable;
import homework.expression.core.Expression;
import javafx.util.Pair;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class TriProdTermTree implements Computable {
    private final BigInteger coef;

    private final TriIndex triIndex;

    private Expression mulExp;

    private TriProdTermTree leftTerm;
    private TriProdTermTree rightTerm;

    TriProdTermTree(BigInteger coef, TriIndex triIndex) {
        this(coef, triIndex, null, null);
    }

    TriProdTermTree(BigInteger coef, TriIndex triIndex,
                    TriProdTermTree leftTerm,
                    TriProdTermTree rightTerm) {
        this.coef = coef;
        this.triIndex = triIndex;
        this.leftTerm = leftTerm;
        this.rightTerm = rightTerm;
    }

    public TriProdTermTree() {
        this(BigInteger.ZERO, TriIndex.ZERO);
    }

    public TriExp toTriExp() {
        TriExp exp = new TriExp();
        for (Pair<TriIndex, BigInteger> pair :
            toPairList()) {
            exp.addAppend(pair.getKey(), pair.getValue());
        }
        return exp;
    }

    public BigInteger getCoef() {
        return coef;
    }

    private TriProdTermTree mergedLeftTree(TriProdTermTree other) {
        if (leftTerm == null) {
            return other.leftTerm;
        }
        if (other.leftTerm == null) {
            return leftTerm;
        }
        return new TriProdTermTree(BigInteger.ZERO, TriIndex.ZERO, leftTerm,
            other.leftTerm);
    }

    private TriProdTermTree mergedRightTree(TriProdTermTree other) {
        if (rightTerm == null) {
            return other.rightTerm;
        }
        if (other.rightTerm == null) {
            return rightTerm;
        }
        return new TriProdTermTree(BigInteger.ZERO, TriIndex.ZERO, rightTerm,
            other.rightTerm);
    }

    private boolean isLeaf() {
        return leftTerm == null & rightTerm == null;
    }

    private List<Pair<TriIndex, BigInteger>> toPairList() {
        List<Pair<TriIndex, BigInteger>> list = new LinkedList<>();
        list.add(new Pair<>(triIndex, coef));
        if (isLeaf()) {
            return list;
        }
        if (leftTerm != null) {
            List<Pair<TriIndex, BigInteger>> left = leftTerm.toPairList();
            list.addAll(left);
        }
        if (rightTerm != null) {
            List<Pair<TriIndex, BigInteger>> right = rightTerm.toPairList();
            list.addAll(right);
        }
        return list;
    }

    @Override
    public Computable add(Computable other) {
        assert other instanceof TriProdTermTree;
        TriProdTermTree otherTerm = (TriProdTermTree) other;
        if (otherTerm.triIndex.equals(triIndex)) {
            return new TriProdTermTree(otherTerm.coef.add(coef), triIndex,
                mergedLeftTree(otherTerm), mergedRightTree(otherTerm));
        } else {
            return new TriProdTermTree(BigInteger.ZERO, TriIndex.ZERO,
                this, otherTerm);
        }
    }

    @Override
    public Computable mul(Computable other) {
        assert other instanceof TriProdTermTree;
        TriProdTermTree otherTerm = (TriProdTermTree) other;
        assert isLeaf() | otherTerm.isLeaf();
        return new TriProdTermTree(otherTerm.coef.multiply(coef),
            otherTerm.triIndex.add(triIndex), mergedLeftTree(otherTerm),
            mergedRightTree(otherTerm));
    }

    @Override
    public Computable pow(Computable index) {
        assert index instanceof TriProdTermTree;
        assert ((TriProdTermTree) index).isLeaf();
        assert ((TriProdTermTree) index).triIndex.isZeroIndex();
        BigInteger powIndex = ((TriProdTermTree) index).coef;
        TriIndex powTriIndex = new TriIndex(powIndex, powIndex, powIndex);
        return new TriProdTermTree(coef, triIndex.mul(powTriIndex), leftTerm,
            rightTerm);
    }

    @Override
    public Computable constant(BigInteger bigInteger) {
        return new TriProdTermTree(bigInteger, TriIndex.ZERO);
    }

    @Override
    public Computable sin() {
        assert isLeaf();
        TriIndex sinIndex = new TriIndex(BigInteger.ZERO, BigInteger.ONE,
            BigInteger.ZERO);
        return new TriProdTermTree(BigInteger.ONE, sinIndex);
    }

    @Override
    public Computable cos() {
        assert isLeaf();
        TriIndex cosIndex = new TriIndex(BigInteger.ZERO, BigInteger.ZERO,
            BigInteger.ONE);
        return new TriProdTermTree(BigInteger.ONE, cosIndex);
    }

    @Override
    public Computable variable() {
        assert isLeaf();
        TriIndex varIndex = new TriIndex(BigInteger.ONE, BigInteger.ZERO,
            BigInteger.ZERO);
        return new TriProdTermTree(BigInteger.ONE, varIndex);
    }
}
