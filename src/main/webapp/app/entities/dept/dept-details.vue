<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dept">
        <h2 class="jh-entity-heading" data-cy="deptDetailsHeading"><span>Dept</span> {{ dept.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Dept Name</span>
          </dt>
          <dd>
            <span>{{ dept.deptName }}</span>
          </dd>
          <dt>
            <span>Dept Address</span>
          </dt>
          <dd>
            <span>{{ dept.deptAddress }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Translation missing for entity.action.back</span>
        </button>
        <router-link v-if="dept.id" :to="{ name: 'DeptEdit', params: { deptId: dept.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Translation missing for entity.action.edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./dept-details.component.ts"></script>
